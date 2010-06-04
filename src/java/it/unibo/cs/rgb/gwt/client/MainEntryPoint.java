/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main entry point.
 *
 * @author fedo
 */
public class MainEntryPoint implements EntryPoint {

    //elementi grafici
    Widget header;
    Widget footer;
    VerticalPanel west = new VerticalPanel();
    VerticalPanel center;
    Label title;
    Widget content;
    TabPanel documentViewerPanel = new TabPanel();
    HTML homepage;
    HorizontalPanel debug = new HorizontalPanel();
    //variabili
    final ArrayList<HashMap> documents = new ArrayList<HashMap>(); //{ String "id", String "path", String "shortName", String "longName", HTML "info", ArrayList<String> "files" }

    /**
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {

        DockPanel mainPanel = new DockPanel();

        mainPanel.setSize("100%", "100%");
        mainPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
        mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        header = createHeaderWidget();
        header.addStyleName("headerPanel");
        header.setHeight("100px");
        mainPanel.add(header, DockPanel.NORTH);
        mainPanel.setCellHeight(header, "100px");

        footer = createFooterWidget();
        mainPanel.add(footer, DockPanel.SOUTH);
        mainPanel.setCellHeight(footer, "25px");

        west = createWestWidget();
        west.setWidth("165px");
        mainPanel.add(west, DockPanel.WEST);
        mainPanel.setCellWidth(west, "165px");

        center = createCenterWidget();
        mainPanel.add(center, DockPanel.CENTER);

        RootPanel.get().add(mainPanel);
    }

    /**
     *
     * @return
     */
    protected Widget createHeaderWidget() {

        return new Label("Header [Branding]: logo + [Servizi stabili]: link help, link contatti");
    }

    /**
     *
     * @return
     */
    protected Widget createFooterWidget() {

        //Label footer = new Label();
        debug.add(new Label("Debug:"));
        debug.setBorderWidth(1);

        return debug;
    }

    private VerticalPanel createCenterWidget() {

        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");

        documentViewerPanel.setSize("100%", "100%");
        documentViewerPanel.addStyleName("table-center");

        content = createHomepage();
        title = new Label("Homepage");

        panel.add(title);
        panel.add(content);

        return panel;
    }

    private Widget createHomepage() {
        homepage = new HTML("<p>Questa è l'home page, clicca su un documento per fare delle storie</p>");
        return homepage;
    }

    /**
     *
     * @return
     */
    protected VerticalPanel createWestWidget() {

        Label title = new Label("Lista documenti");

        west.add(title);
        createDocumentsList();

        return west;
    }

    /**
     * 
     * @param teipath
     * @return
     */
    protected Widget createDocumentViewerTab(final String path) {

        final HashMap hashMap = getDocumentHashMap(path);
        String id = (String) hashMap.get("id");
        final VerticalPanel panel = new VerticalPanel();
        panel.setTitle(id);

        //DocumentInfo
        String url = "http://localhost:8080/RgB/DocumentInfo";
        String postData = URL.encode("path") + "=" + URL.encode(path);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postData, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    //NOTE gran bei cazzi con le variabili Final e con le variabili globali... vedere come fatto (per culo) per "documents" (arraylist)
                    //NOTE gli oggetti che crei dentro alla sendrequest devono essere modificati solo dentro la request

                    Document responseXml = XMLParser.parse(response.getText().toString());
                    String longName = (String) hashMap.get("longName");
                    String witnesses = (String) hashMap.get("witnesses");

                    panel.add(new Label((String) hashMap.get("shortName")));
                    panel.add(new Label("BARRA DEI COMANDI ServizioX ServizioY ServizioZ ServizioK"));

                    //info
                    DisclosurePanel info = new DisclosurePanel("Informazioni sul documento");
                    info.setWidth("100%");
                    info.setOpen(true);
                    info.setContent(new HTML(responseXml.getElementById("info").getFirstChild().toString()));
                    panel.add(info);

                    //debug.clear();
                    //debug.add(new Label(response.getText().toString()));

                    //witnesses
                    HorizontalPanel witnessesPanel = new HorizontalPanel();
                    if(witnesses.split(", ").length > 0)
                        witnessesPanel.add(new Label("Visualizza testimoni: "));
                    else if (witnesses.equalsIgnoreCase(""))
                        witnessesPanel.add(new Label("Visualizza il documento: "));
                    //if(!witnesses.equals(""))
                        panel.add(witnessesPanel);

                    final HorizontalPanel witnessViewer = new HorizontalPanel();
                    witnessViewer.setBorderWidth(1);
                    panel.add(witnessViewer);


                    //NodeList witnessesList = XMLParser.parse(responseXml.getElementById("witnesses").toString()).getElementsByTagName("li");
                    for (int i = 0; i < witnesses.split(", ").length; i++) {
                        final String witness = witnesses.split(", ")[i];
                        final CheckBox checkbox = new CheckBox(witness);

                        //durante la crezione, checkbox è true e creo la prima visualizzazione
                        if (i == 0) {
                            checkbox.setValue(true);
                            witnessViewer.add(requestDocumentView(path, witness));
                        }

                        checkbox.addClickHandler(new ClickHandler() {

                            public void onClick(ClickEvent event) {
                                if (checkbox.getValue()) {
                                    witnessViewer.add(requestDocumentView(path, witness));
                                } else {
                                    witnessViewer.remove(getChildWidgetIndex(witnessViewer, witness));
                                }
                            }
                        });
                        witnessesPanel.add(checkbox);
                    }
                }

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentInfo");
                }
            });
        } catch (RequestException ex) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentInfo (Couldn't connect to server)");
        }

        return panel;
    }

    /**
     * Restituisce l'HashMap del documento associato all'absolutePath
     * @param path
     * @return
     */
    private HashMap getDocumentHashMap(String path) {

        HashMap retval = new HashMap();

        for (int i = 0; i < documents.size(); i++) {
            if (((String) documents.get(i).get("path")).equalsIgnoreCase(path)) {
                retval = documents.get(i);
                break;
            }
        }

        return retval;
    }

    private void createDocumentsList() {
        //DocumentList
        String url = "http://localhost:8080/RgB/DocumentsList";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        //xhtml parsing
                        Document responseXml = XMLParser.parse(response.getText().toString());

                        int numberOfDocuments = new Integer(responseXml.getElementById("numberOfDocuments").getFirstChild().toString());
                        //west.add(new Label("numero documenti: "+lenght));

                        for (int i = 0; i < numberOfDocuments; i++) {
                            HashMap documentInfo = new HashMap();
                            documents.add(documentInfo);

                            String current = responseXml.getElementsByTagName("li").item(i).toString();
                            Document currentXml = XMLParser.parse(current);

                            //estrapolazione dati dalla servlet
                            final String id = new HTML(currentXml.getElementById("id").getFirstChild().toString()).getHTML();
                            documentInfo.put("id", id);

                            final String path = new HTML(currentXml.getElementById("path").getFirstChild().toString()).getHTML();
                            documentInfo.put("path", path);

                            String doctitle = id;
                            if (current.contains("docTitle")) {
                                doctitle = new HTML(currentXml.getElementById("docTitle").getFirstChild().toString()).getHTML();
                            }
                            documentInfo.put("title", doctitle);


                            String author = "(autore mancante)";
                            if (current.contains("docAuthor")) {
                                author = new HTML(currentXml.getElementById("docAuthor").getFirstChild().toString()).getHTML();
                            }
                            documentInfo.put("author", author);

                            String witnesses = "";
                            if (current.contains("docWit")) {
                                witnesses = new HTML(currentXml.getElementById("docWit").getFirstChild().toString()).getHTML();
                            }
                            documentInfo.put("witnesses", witnesses);

                            //final String shortName = new HTML(currentXml.getElementById("shortName").getFirstChild().toString()).getHTML();
                            final String shortName = doctitle;
                            documentInfo.put("shortName", shortName);

                            final String longName = currentXml.getElementById("longName").toString() + "<b>Nome file:</b> " + id;
                            documentInfo.put("longName", longName);

                            //*** disegno bottoni del menu doclist
                            final Label currentWidget;
                            currentWidget = new Label(shortName);

                            //finestra contenente nome lungo del documento
                            final PopupPanel overWidget = new PopupPanel(true);
                            overWidget.setVisible(false);
                            overWidget.add(new HTML(longName));

                            MouseOverHandler overHandler = new MouseOverHandler() {

                                public void onMouseOver(final MouseOverEvent event) {
                                    currentWidget.addStyleName("buttonDocumentsListOver");
                                    overWidget.setPopupPosition(event.getClientX(), event.getClientY());
                                    overWidget.setPopupPositionAndShow(new PositionCallback() {

                                        public void setPosition(int offsetWidth, int offsetHeight) {
                                            //int left = event.getClientX() + 50;
                                            int left = 165;
                                            int top = event.getClientY();
                                            overWidget.setPopupPosition(left, top);
                                        }
                                    });

                                }
                            };

                            MouseOutHandler outHandler = new MouseOutHandler() {

                                public void onMouseOut(MouseOutEvent event) {
                                    currentWidget.removeStyleName("buttonDocumentsListOver");
                                    overWidget.hide();
                                }
                            };


                            currentWidget.addStyleName("buttonDocumentsList");
                            currentWidget.setTitle(id);
                            currentWidget.addMouseOverHandler(overHandler);
                            currentWidget.addMouseOutHandler(outHandler);
                            //TODO puntatore

                            //handler del click: creazione/selezione tab
                            currentWidget.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {

                                    if (content != documentViewerPanel) {
                                        center.remove(content);
                                        content = documentViewerPanel;
                                        center.add(content);
                                        title.setText("Visualizzatore Documenti Tei");
                                    }

                                    int index = getTabIndex(id);

                                    // tab non esiste
                                    if (index < 0) {

                                        // contenuto del tab
                                        final Widget tabPanel = createDocumentViewerTab(path);

                                        // tabText
                                        HorizontalPanel tabText = new HorizontalPanel();

                                        //TODO: visualizzare un tabText "carino"
                                        Label tabTitle = new Label(shortName);
                                        tabTitle.setWidth(shortName.length() * 8 + "px");
                                        tabText.add(tabTitle);

                                        // X che chiude il tab
                                        ClickHandler closeHandler = new ClickHandler() {

                                            public void onClick(ClickEvent event) {

                                                int indexToClose = getTabIndex(id);
                                                int indexSelectedTab = documentViewerPanel.getTabBar().getSelectedTab();
                                                String idSelectedTab = documentViewerPanel.getWidget(indexSelectedTab).getTitle();

                                                documentViewerPanel.remove(tabPanel);

                                                if (documentViewerPanel.getWidgetCount() == 0) { //DocumentViewer è vuoto
                                                    center.remove(content);
                                                    content = homepage;
                                                    center.add(content);
                                                    title.setText("Homepage");
                                                } else { //DocumentViewer non è vuoto, quando premi "x" selezioni comunque
                                                    if (indexToClose == indexSelectedTab) {
                                                        documentViewerPanel.selectTab(Math.max(indexToClose - 1, 0));
                                                    } else {
                                                        documentViewerPanel.selectTab(getTabIndex(idSelectedTab));
                                                    }
                                                }
                                            }
                                        };
                                        HTML x = new HTML("<b>x</b>");
                                        x.addClickHandler(closeHandler);
                                        tabText.add(x);

                                        documentViewerPanel.add(tabPanel, tabText);
                                        documentViewerPanel.selectTab(documentViewerPanel.getWidgetCount() - 1);
                                    }
                                }
                            });

                            west.add(currentWidget);
                        }
                    } else {
                        Window.alert("ERRORE: la risposta del servizio DocumentList non è quella aspettata");
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
        }
    }

    private int getChildWidgetIndex(ComplexPanel panel, String title) {
        int index = panel.getWidgetCount() - 1;
        while (index >= 0) {
            if (panel.getWidget(index).getTitle().equalsIgnoreCase(title)) {
                break;
            }
            index--;
        }
        //debug.add(new Label("childddd " + index));
        return index;
    }

    private int getTabIndex(String id) {
        int index = documentViewerPanel.getWidgetCount() - 1;
        while (index >= 0) {
            if (documentViewerPanel.getWidget(index).getTitle().equalsIgnoreCase(id)) {
                documentViewerPanel.selectTab(index);
                break;
            }
            index--;
        }
        return index;
    }

    private Widget requestDocumentView(String path, String witness) {

        final HTML retval = new HTML();
        retval.setTitle(witness);

        //DocumentViewer
        String url = "http://localhost:8080/RgB/DocumentViewer?acaso=123";
        String postData = URL.encode("path") + "=" + URL.encode(path) + "&" + URL.encode("witness") + "=" + URL.encode(witness);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postData, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        //xhtml parsing
                        Document responseXml = XMLParser.parse(response.getText().toString());


                        retval.setHTML(responseXml.toString());



                    } else {
                        Window.alert("ERRORE: la risposta del servizio DocumentList non è quella aspettata");
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
        }


        return retval;
    }
}
