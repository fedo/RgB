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
import com.google.gwt.user.client.DOM;
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
    TabPanel documentViewPanel = new TabPanel();
    Widget header;
    Widget footer;
    VerticalPanel west = new VerticalPanel();
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

        mainPanel.setBorderWidth(5);
        mainPanel.setSize("100%", "100%");
        mainPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
        mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        header = createHeaderWidget();
        mainPanel.add(header, DockPanel.NORTH);
        mainPanel.setCellHeight(header, "100px");

        footer = createFooterWidget();
        mainPanel.add(footer, DockPanel.SOUTH);
        mainPanel.setCellHeight(footer, "25px");

        west = createWestWidget();
        mainPanel.add(west, DockPanel.WEST);
        mainPanel.setCellWidth(west, "150px");

        Widget content = createContentWidget();
        mainPanel.add(content, DockPanel.CENTER);
        RootPanel.get().add(mainPanel);
    }

    /**
     *
     * @return
     */
    protected Widget createHeaderWidget() {

        Label header = new Label("Header [Branding]: logo + [Servizi stabili]: link help, link contatti");

        return header;
    }

    /**
     *
     * @return
     */
    protected Widget createFooterWidget() {

        Label footer = new Label();

        debug.add(new Label("Debug:"));

        return debug;
    }

    /**
     *
     * @return
     */
    protected Widget createContentWidget() {

        documentViewPanel.setSize("100%", "250px");
        documentViewPanel.addStyleName("table-center");

        return documentViewPanel;
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
    protected Widget createDocumentViewerTab(String path) {

        VerticalPanel panel = new VerticalPanel();
        panel.setTitle((String) getDocumentHashMap(path).get("id"));
        panel.add(new Label("risultato " + (String) getDocumentHashMap(path).get("path")));


        //Contesto: nome breve del documento + elenco delle sigle delle lezioni varianti disponibili + barra di comando

        //nome breve
        //lezioni varianti disponibili
        //comandi
        //

        //DocumentList
        String url = "http://localhost:8080/RgB/DocumentInfo";
        String postData = URL.encode("path") + "=" + URL.encode(path);


        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postData, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    Document responseXml = XMLParser.parse(response.getText().toString());
                    HTML info = new HTML(responseXml.getElementById("info").toString());
                    //TODO inserire in documentInfo

                    debug.clear();
                    debug.add(new Label(response.getText()));

                    //TODO riempire anche la lista dei witness

                }

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentInfo");
                }
            });
        } catch (RequestException ex) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentInfo (Couldn't connect to server)");
        }

        //nome lungo
        final DisclosurePanel info = new DisclosurePanel("Clicca qui per visualizzare le informazioni complete");
        info.add(new Label("dati lunghi lunghi lunghi"));
        //TODO tirare fuori da documentInfo
        panel.add(info);

        //witnesses
        final ArrayList<String> witnessesList = new ArrayList<String>();
        HashMap document = getDocumentHashMap(path);
        document.put("witnessesList",witnessesList);
        //fillWitnessesList(witnessesList);
        if(witnessesList.size() == 1)
            debug.add(new Label("un solo witness, unique")); //TODO
        else if(witnessesList.size() > 1)
            debug.add(new Label("più witnesses")); //TODO


        HorizontalPanel witnessesPanel = new HorizontalPanel();
        witnessesPanel.add(new Label("Scegli i witness da visualizzare: "));



        /*

        // TODO: abilitazione dinamica in base al documento (alcuni testi non supportano certe visualizzazioni
        // TODO: aggiungere listeners che ricaricano i wit aperti in accordo con la vis. scelta
        ListBox visualization = new ListBox();
        visualization.addItem("diplomatica");
        visualization.addItem("Semi-diplomatica");
        visualization.addItem("critica");
        visualizationAndWitnesses.add(new Label("Scegli il tipo di trascrizione: "));
        visualizationAndWitnesses.add(visualization);

        CheckBox cb = new CheckBox("testwit");

        // clickhandler per le checkbox
        cb.addClickHandler(new ClickHandler() {

        public void onClick(ClickEvent event) {
        boolean checked = ((CheckBox) event.getSource()).getValue();
        witnesses.add(new Label("testwit"));
        }
        });

        visualizationAndWitnesses.add(cb);
        visualizationAndWitnesses.add(new CheckBox("wit1"));
        visualizationAndWitnesses.add(new CheckBox("wit2"));
        visualizationAndWitnesses.add(new CheckBox("wit3"));

        panel.add(visualizationAndWitnesses);

        // visualizzazione dei witnesses selezionati
        panel.add(witnesses);

        return panel;

         */
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
                            

                            final String id = new HTML(currentXml.getElementById("id").getFirstChild().toString()).getHTML();
                            documentInfo.put("id", id);
                            final String path = new HTML(currentXml.getElementById("path").getFirstChild().toString()).getHTML();
                            documentInfo.put("path", path);
                            final String shortName = new HTML(currentXml.getElementById("shortName").getFirstChild().toString()).getHTML();
                            documentInfo.put("shortName", shortName);
                            final String longName = new HTML(currentXml.getElementById("longName").getFirstChild().toString()).getHTML();
                            documentInfo.put("shortName", longName);

                            final Label currentWidget;
                            currentWidget = new Label(shortName);

                            //finestra contenente nome lungo del documento
                            final PopupPanel overWidget = new PopupPanel(true);
                            overWidget.setVisible(false);
                            overWidget.add(new Label(longName));

                            MouseOverHandler overHandler = new MouseOverHandler() {

                                public void onMouseOver(final MouseOverEvent event) {
                                    currentWidget.addStyleName("buttonDocumentsListOver");
                                    overWidget.setPopupPosition(event.getClientX(), event.getClientY());
                                    overWidget.setPopupPositionAndShow(new PositionCallback() {

                                        public void setPosition(int offsetWidth, int offsetHeight) {
                                            //int left = event.getClientX() + 50;
                                            int left = 150;
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
                                    int index = documentViewPanel.getWidgetCount() - 1;

                                    // cerco il tab
                                    while (index >= 0) {
                                        if (documentViewPanel.getWidget(index).getTitle().equalsIgnoreCase(id)) {
                                            documentViewPanel.selectTab(index);
                                            break;
                                        }
                                        index--;

                                    }

                                    // tab non esiste
                                    if (index < 0) {

                                        // contenuto del tab
                                        final Widget tabPanel = createDocumentViewerTab(path);

                                        // tabText
                                        HorizontalPanel tabText = new HorizontalPanel();

                                        //TODO: visualizzare un tabText "carino"
                                        Label tabTitle = new Label(shortName);
                                        tabTitle.setWidth(shortName.length()*8+"px");
                                        tabText.add(tabTitle);

                                        // X che chiude il tab
                                        ClickHandler closeHandler = new ClickHandler() {

                                            public void onClick(ClickEvent event) {
                                                documentViewPanel.remove(tabPanel);
                                            }
                                        };
                                        HTML x = new HTML("<b>x</b>");
                                        x.addClickHandler(closeHandler);
                                        tabText.add(x);

                                        documentViewPanel.add(tabPanel, tabText);
                                        documentViewPanel.selectTab(documentViewPanel.getWidgetCount() - 1);
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
}
