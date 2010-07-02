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
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Main entry point.
 *
 * @author fedo
 */
public class MainEntryPoint implements EntryPoint {

    // dock
    Widget headerPanel;
    Widget footerPanel;
    VerticalPanel westPanel = new VerticalPanel();
    VerticalPanel centerPanelOld = new VerticalPanel();
    // central
    Label title;
    Widget content;
    SimplePanel centerPanel = new SimplePanel();
    // pages
    DecoratedTabPanel documentViewerPanel = new DecoratedTabPanel();
    HTML homehtml;
    VerticalPanel homepage = new VerticalPanel();
    VerticalPanel documentsViewer = new VerticalPanel();
    VerticalPanel help = new VerticalPanel();
    VerticalPanel contacts = new VerticalPanel();
    // footer
    HorizontalPanel debug = new HorizontalPanel();
    // variables
    final ArrayList<HashMap> documents = new ArrayList<HashMap>(); //{ String "id", String "path", String "shortName", String "longName", HTML "info", ArrayList<String> "files" }
    String host;
    boolean documentsListReady = false;
    int scrollY = 0;

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

        host = Window.Location.getHost();

        DockPanel mainPanel = new DockPanel();

        mainPanel.setSize("100%", "100%");
        mainPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
        mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        headerPanel = createHeaderWidget();
        headerPanel.addStyleName("headerPanel");
        headerPanel.setHeight("125px");
        mainPanel.add(headerPanel, DockPanel.NORTH);
        mainPanel.setCellHeight(headerPanel, "125px");

        footerPanel = createFooterWidget();
        mainPanel.add(footerPanel, DockPanel.SOUTH);
        mainPanel.setCellHeight(footerPanel, "25px");

        westPanel = createWestWidget();
        westPanel.addStyleName("westPanel");
        westPanel.setWidth("200px");
        mainPanel.add(westPanel, DockPanel.WEST);
        mainPanel.setCellWidth(westPanel, "200px");

        createPages();

        centerPanelOld = createCenterWidget();
        centerPanelOld.addStyleName("centerPanel");

        centerPanel.setSize("100%", "100%");
        centerPanel.setWidget(homepage);
        mainPanel.add(centerPanel, DockPanel.CENTER);

        RootPanel.get().add(mainPanel);
    }

    /**
     *
     * @return
     */
    protected Widget createHeaderWidget() {

        VerticalPanel panel = new VerticalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        HorizontalPanel menu = new HorizontalPanel();
        menu.addStyleName("menuHeader");

        Label tohomepage = new Label("Homepage");
        Label tohelp = new Label("Aiuto");
        Label tocontacts = new Label("Contatti");

        tohomepage.setStyleName("menuElement");
        tohelp.setStyleName("menuElement");
        tocontacts.setStyleName("menuElement");

        tohomepage.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                centerPanel.setWidget(homepage);
            }
        });
        tohelp.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                centerPanel.setWidget(help);
            }
        });
        tocontacts.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                centerPanel.setWidget(contacts);
            }
        });

        menu.add(tohomepage);
        menu.add(tohelp);
        menu.add(tocontacts);

        panel.add(menu);

        return panel;
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
        homehtml = new HTML("<p>Questa è l'home page, clicca su un documento per fare delle storie: " + host + "</p>");
        return homehtml;
    }

    /**
     *
     * @return
     */
    protected VerticalPanel createWestWidget() {

        Label listTitle = new Label("Lista documenti");
        listTitle.addStyleName("buttonDocumentsListHeader");
        westPanel.add(listTitle);
        createDocumentsList();

        return westPanel;
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
        panel.setSize("100%", "100%");
        panel.setTitle(id);

        //DocumentInfo
        String url = "http://" + host + "/RgB/DocumentInfo";
        String postData = URL.encode("path") + "=" + URL.encode(path);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postData, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    //NOTE gli oggetti che crei dentro alla sendrequest devono essere modificati solo dentro la request

                    Document responseXml = XMLParser.parse(response.getText().toString());
                    String longName = (String) hashMap.get("longName");
                    String witnesses = (String) hashMap.get("witnesses");

                    // * informazione documenti
                    DisclosurePanel info = new DisclosurePanel("Informazioni sul documento");
                    info.setWidth("100%");
                    info.setOpen(true);
                    final HTML loading = new HTML("<img src=\"images/loading.gif\" alt=\"Caricamento in corso\" />");
                    loading.setStyleName("loading");
                    info.setContent(loading);
                    info.setContent(new HTML(responseXml.getElementById("info").getFirstChild().toString()));
                    panel.add(info);
                    panel.add(new HTML("<br/>"));

                    // * servizi
                    DisclosurePanel servicesDisclosure = new DisclosurePanel("Servizi");
                    servicesDisclosure.setWidth("100%");
                    servicesDisclosure.setOpen(true);
                    VerticalPanel servicesContent = new VerticalPanel();
                    servicesContent.setWidth("100%");
                    Panel serviceActive = new VerticalPanel();
                    serviceActive.setWidth("100%");
                    serviceActive.setVisible(false);

                    // ** servizi: bottoni
                    HorizontalPanel serviceButtons = new HorizontalPanel();
                    serviceButtons.add(createFrequenzeDiOccorrenzaButton(serviceActive, path));
                    serviceButtons.add(createColocazioniButton(serviceActive, path));
                    serviceButtons.add(createStemmaCodicumButton(serviceActive, path));
                    serviceButtons.add(createEstrazioneDiConcordanzeButton(serviceActive, path));
                    serviceButtons.add(createDifferenziazioneButton(serviceActive, path));
                    serviceButtons.add(createCloseAllServicesButton(serviceActive));

                    // ** servizi: annidamento
                    servicesContent.add(serviceButtons);
                    servicesContent.add(serviceActive);
                    servicesDisclosure.add(servicesContent);
                    panel.add(servicesDisclosure);
                    panel.add(new HTML("<br/>"));


                    // * visualizzazione
                    DisclosurePanel viewerDisclosure = new DisclosurePanel("Visualizzatore documento");
                    viewerDisclosure.setWidth("100%");
                    viewerDisclosure.setOpen(true);
                    VerticalPanel viewerContent = new VerticalPanel();
                    viewerContent.setWidth("100%");
                    HorizontalPanel witnessesCheckboxList = new HorizontalPanel();
                    final HorizontalPanel witnessViewer = new HorizontalPanel();
                    witnessViewer.setWidth("100%");

                    // ** annidamento
                    viewerContent.add(witnessesCheckboxList);
                    viewerContent.add(witnessViewer);
                    viewerDisclosure.add(viewerContent);
                    panel.add(viewerDisclosure);

                    // ** lista checkbox witnesses
                    if (witnesses.split(", ").length > 1) {
                        witnessesCheckboxList.add(new Label("Visualizza testimoni: "));
                        witnessesCheckboxList.setVisible(true);
                    } else {
                        witnessesCheckboxList.add(new Label("Visualizza il documento: "));
                        witnessesCheckboxList.setVisible(false);
                    }

                    // *** checkbox witness
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
                                    Widget reqpanel = requestDocumentView(path, witness);
                                    witnessViewer.add(reqpanel);
                                } else {
                                    witnessViewer.remove(getChildWidgetIndex(witnessViewer, witness));
                                }
                            }
                        });
                        witnessesCheckboxList.add(checkbox);
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
        String url = "http://" + host + "/RgB/DocumentsList";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

        final HTML loading = new HTML("<img src=\"images/loading.gif\" alt=\"Caricamento in corso\" />");
        loading.setStyleName("loading");
        westPanel.add(loading);

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
                            String id = new HTML(currentXml.getElementById("id").getFirstChild().toString()).getHTML();
                            documentInfo.put("id", id);

                            String path = new HTML(currentXml.getElementById("path").getFirstChild().toString()).getHTML();
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

                            String shortName = doctitle;
                            documentInfo.put("shortName", shortName);

                            String longName = currentXml.getElementById("longName").toString() + "<b>Nome file:</​b> " + id;
                            documentInfo.put("longName", longName);
                        }

                        ArrayList<String> idArray = new ArrayList<String>();
                        ArrayList<HashMap> ordDoc = new ArrayList<HashMap>();
                        int docLength = documents.size();
                        for (int i = 0; i < docLength; i++) {
                            idArray.add(((String) documents.get(i).get("id")));
                        }

                        Collections.sort(idArray);
                        for (int i = 0; i < docLength; i++) {
                            for (int d = 0; d < docLength; d++) {
                                if (idArray.get(i).equalsIgnoreCase(((String) documents.get(d).get("id")))) {
                                    ordDoc.add(documents.get(d));
                                }
                            }
                        }

                        for (int i = 0; i < docLength; i++) {
                            //*** disegno bottoni del menu doclist
                            final Label currentWidget;
                            final String path = (String) ordDoc.get(i).get("path");
                            final String shortName = (String) ordDoc.get(i).get("shortName");
                            currentWidget = new Label(shortName);

                            //finestra contenente nome lungo del documento
                            final PopupPanel overWidget = new PopupPanel(true);
                            overWidget.setVisible(false);
                            final String longName = (String) ordDoc.get(i).get("longName");
                            overWidget.add(new HTML(longName));

                            MouseOverHandler overHandler = new MouseOverHandler() {

                                public void onMouseOver(final MouseOverEvent event) {
                                    currentWidget.addStyleName("buttonDocumentsListOver");

                                    overWidget.setPopupPositionAndShow(new PositionCallback() {

                                        public void setPosition(int offsetWidth, int offsetHeight) {
                                            //int left = event.getClientX() + 50;
                                            int left = 200;
                                            int top = event.getClientY() + Window.getScrollTop() - 20;
                                            debug.clear();
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
                            final String id = (String) ordDoc.get(i).get("id");
                            currentWidget.setTitle(id);
                            currentWidget.addMouseOverHandler(overHandler);
                            currentWidget.addMouseOutHandler(outHandler);

                            //handler del click: creazione/selezione tab
                            currentWidget.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {

                                    centerPanel.setWidget(documentsViewer);

                                    int index = getTabIndex(id);

                                    // tab non esiste
                                    if (index < 0) {

                                        // contenuto del tab
                                        final Widget tabPanel = createDocumentViewerTab(path);

                                        // tabText
                                        HorizontalPanel tabText = new HorizontalPanel();

                                        Label tabTitle = new Label(shortName);
                                        tabTitle.setWidth(shortName.length() * 8 + "px");
                                        tabText.add(tabTitle);
                                        tabText.setStyleName("tabTitle");

                                        // X che chiude il tab
                                        ClickHandler closeHandler = new ClickHandler() {

                                            public void onClick(ClickEvent event) {

                                                int indexToClose = getTabIndex(id);
                                                int indexSelectedTab = documentViewerPanel.getTabBar().getSelectedTab();
                                                String idSelectedTab = documentViewerPanel.getWidget(indexSelectedTab).getTitle();

                                                documentViewerPanel.remove(tabPanel);

                                                if (documentViewerPanel.getWidgetCount() == 0) { //DocumentViewer è vuoto
                                                    centerPanel.setWidget(homepage);
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

                            westPanel.add(currentWidget);
                            if (i == numberOfDocuments - 1) {
                                westPanel.remove(loading);
                                documentsListReady = true;
                            }
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

        VerticalPanel retval = new VerticalPanel();
        retval.setWidth("100%");
        retval.setTitle(witness);
        retval.setStyleName("documentView");

        final HorizontalPanel documentPanel = new HorizontalPanel();
        documentPanel.setWidth("100%");

        final HTML text = new HTML("<p>Caricamento testo...</p>");
        text.setWidth("80%");
        final HTML notes = new HTML("<p>Caricamento note...</p>");
        notes.setWidth("165px");

        // ** visualizzazione witnesses
        if (!witness.equalsIgnoreCase("")) {
            HTML selectedWitness = new HTML("<h3>Testimone: " + witness);
            selectedWitness.setStyleName("documentViewerWitness");
            retval.add(selectedWitness);

        }

        // * checkbox note
        final CheckBox noteCheckbox = new CheckBox("visualizza note");
        noteCheckbox.setValue(true);
        noteCheckbox.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (noteCheckbox.getValue()) {
                    notes.setVisible(true);
                } else {
                    notes.setVisible(false);
                }
            }
        });

        // ** annidamento
        documentPanel.add(text);
        documentPanel.add(notes);
        retval.add(noteCheckbox);
        retval.add(documentPanel);

        // visualizzazione
        String url = "http://" + host + "/RgB/DocumentViewer";
        String postData = URL.encode("path") + "=" + path + "&" + URL.encode("witness") + "=" + witness + "&" + URL.encode("service") + "=visualizzazione";
        //text.setHTML(postData);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postData, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        text.setHTML("<div class=\"viewerTextSpace\"></div>" + response.getText());
                    } else {
                        Window.alert("ERRORE: la risposta del servizio DocumentList non è quella aspettata");
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
        }

        // note
        String urlNote = "http://" + host + "/RgB/DocumentViewer";
        String postDataNote = URL.encode("path") + "=" + path + "&" + URL.encode("witness") + "=" + witness + "&" + URL.encode("service") + "=note";

        RequestBuilder builderNote = new RequestBuilder(RequestBuilder.POST, URL.encode(urlNote));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(postDataNote, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {

                        if (response.getText().contains("<body></body>")) {
                            documentPanel.remove(notes);
                            noteCheckbox.setVisible(false);

                        } else {
                            notes.setHTML("<div class=\"notesTextSpace\"></div>" + response.getText());
                        }
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

    public Widget createCloseAllServicesButton(final Panel service) {
        Button button = new Button("Chiudi tutti i servizi");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                service.clear();
                service.setVisible(false);
            }
        });
        return button;
    }

    public Widget createFrequenzeDiOccorrenzaButton(final Panel service, final String path) {
        Button button = new Button("Frequenze di occorrenza");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                service.clear();
                service.setVisible(true);
                service.add(new Label("Visualizzatore della lista e della frequenza di tutte le colocazioni di una parola."));
                //ScrollPanel fScrollPanel = new ScrollPanel(makeFrequenzeDiOccorrenzaRequest(path));

                Frame frame = new Frame("http://" + host + "/RgB/Dispatcher?service=FrequenzeDiOccorrenza&path=" + path);
                frame.setSize("100%", "375px");
                service.add(frame);

                Button closeButton = new Button("Chiudi il servizio");
                closeButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        service.clear();
                        service.setVisible(false);
                    }
                });


                service.add(closeButton);

            }
        });
        return button;
    }

    public Widget createColocazioniButton(final Panel service, final String path) {
        Button button = new Button("Colocazioni");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                final Button closeButton = new Button("Chiudi il servizio");
                closeButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        service.clear();
                        service.setVisible(false);
                    }
                });

                service.clear();
                service.setVisible(true);

                VerticalPanel form = new VerticalPanel();
                final TextBox wordBox = new TextBox();

                Button serviceButton = new Button("Invia");
                serviceButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        if (wordBox.getText().equalsIgnoreCase("")) {
                            Window.alert("Il servizio richiede una parola da ricercare. Inseriscila nella TextBox e clicca di nuovo il pulsante.");
                        } else {
                            service.clear();
                            service.setVisible(true);
                            service.add(makeColocazioniRequest(path, wordBox.getText()));
                            service.add(closeButton);
                        }

                    }
                });

                form.add(new Label("Inserisci la parola da ricercare:"));
                form.add(wordBox);
                form.add(serviceButton);

                service.add(new Label("Visualizzatore della lista e della frequenza di tutte le colocazioni di una parola."));
                service.add(form);
                service.add(closeButton);

            }
        });
        return button;
    }

    public Widget createEstrazioneDiConcordanzeButton(final Panel service, final String path) {
        Button button = new Button("Estrazione di concordanze");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                final Button closeButton = new Button("Chiudi il servizio");
                closeButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        service.clear();
                        service.setVisible(false);
                    }
                });

                service.clear();
                service.setVisible(true);

                VerticalPanel form = new VerticalPanel();
                final TextBox wordBox = new TextBox();
                final TextBox numberBox = new TextBox();

                Button serviceButton = new Button("Invia");
                serviceButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        if (wordBox.getText().equalsIgnoreCase("") || numberBox.getText().equalsIgnoreCase("") || !numberBox.getText().matches("[0-9]+")) {
                            Window.alert("Il servizio richiede una parola da ricercare. Inseriscila nella TextBox e clicca di nuovo il pulsante.");
                        } else {
                            service.clear();
                            service.setVisible(true);


                            ArrayList<HashMap> params = new ArrayList<HashMap>();
                            HashMap pathMap = new HashMap();
                            pathMap.put("name", "path");
                            pathMap.put("value", path);
                            HashMap serviceMap = new HashMap();
                            serviceMap.put("name", "service");
                            serviceMap.put("value", "EstrazioneDiConcordanze");
                            HashMap wordMap = new HashMap();
                            wordMap.put("name", "word");
                            wordMap.put("value", wordBox.getText());
                            HashMap numberMap = new HashMap();
                            numberMap.put("name", "number");
                            numberMap.put("value", numberBox.getText());

                            params.add(pathMap);
                            params.add(serviceMap);
                            params.add(numberMap);
                            params.add(wordMap);

                            service.add(makeDispatcherRequest(params));
                            service.add(closeButton);
                        }

                    }
                });


                form.add(new Label("Inserisci la parola da ricercare:"));
                form.add(wordBox);
                form.add(new Label("Numero di parole prima e di parole dopo da visualizzare:"));
                form.add(numberBox);
                form.add(serviceButton);

                service.add(new Label("Motore di ricerca per estrazione di concordanze."));
                service.add(form);
                service.add(closeButton);

            }
        });
        return button;
    }

    public Widget createStemmaCodicumButton(final Panel service, final String path) {
        Button button = new Button("StemmaCodicum");
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                service.clear();
                service.setVisible(true);

                Button closeButton = new Button("Chiudi il servizio");
                closeButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        service.clear();
                        service.setVisible(false);
                    }
                });

                service.add(new Label("Visualizzatore dello Stemma Codicum, l'albero delle versioni (tramite testimoni) di un documento."));
                Frame frame = new Frame("http://" + host + "/RgB/Dispatcher?service=StemmaCodicum&path=" + path);
                frame.setSize("100%", "375px");
                service.add(frame);
                /*service.add(makeStemmaCodicumRequest(path));*/
                service.add(closeButton);

            }
        });

        return button;
    }

    public Widget makeStemmaCodicumRequest(String path) {

        final HTML retval = new HTML();

        String url = "http://" + host + "/RgB/Dispatcher?service=StemmaCodicum&path=" + path;

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        retval.setHTML(response.getText().toString());
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

    public Widget makeFrequenzeDiOccorrenzaRequest(String path) {

        final ArrayList<String[]> rows = new ArrayList<String[]>();

        final HTML retval = new HTML();

        String url = "http://" + host + "/RgB/Dispatcher?service=FrequenzeDiOccorrenza&path=" + path;

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {

                        Document responseXml = XMLParser.parse(response.getText().toString());
                        NodeList tr = responseXml.getElementsByTagName("tr");
                        for (int i = 0; i < tr.getLength(); i++) {
                            String[] row = new String[2];
                            row[1] = "" + tr.item(i).getFirstChild().getFirstChild().toString();
                            row[2] = "" + tr.item(i).getLastChild().getFirstChild().toString();
                            rows.add(row);

                        }



                        retval.setHTML(response.getText().toString());
                    } else {
                        Window.alert("ERRORE: la risposta del servizio DocumentList non è quella aspettata");
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
        }

        for (int i = 0; i < rows.size(); i++) {
            //retval.setHTML(retval.getHTML()+"<p>"+rows.get(i)[1]+"</p>");
        }
        return retval;
    }

    public Widget makeColocazioniRequest(String path, String word) {

        final HTML retval = new HTML();

        String url = "http://" + host + "/RgB/Dispatcher?service=Colocazioni&path=" + path + "&word=" + word;

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        retval.setHTML(response.getText().toString());
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

    /*public Widget makeEstrazioneDiConcordanzeRequest(String path, String word, String number) {

    final HTML retval = new HTML();

    String url = "http://" + host + "/RgB/Dispatcher?service=EstrazioneDiConcordanze&path=" + path + "&word=" + word + "&number=" + number;

    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
    builder.setHeader("Content-type", "application/x-www-form-urlencoded");
    try {
    Request request = builder.sendRequest(null, new RequestCallback() {

    public void onError(Request request, Throwable exception) {
    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
    }

    public void onResponseReceived(Request request, Response response) {
    if (200 == response.getStatusCode()) {
    retval.setHTML(response.getText().toString());
    } else {
    Window.alert("ERRORE: la risposta del servizio DocumentList non è quella aspettata");
    }
    }
    });
    } catch (RequestException e) {
    Window.alert("ERRORE: fallita richiesta servizio DocumentList (Couldn't connect to server)");
    }
    return retval;
    }*/
    public Label makeDispatcherRequest(ArrayList<HashMap> params) {

        final HTML retval = new HTML();

        String url = "http://" + host + "/RgB/Dispatcher";
        for (int i = 0; i < params.size(); i++) {
            HashMap hm = params.get(i);
            if (i == 0) {
                url = url + "?";
            } else {
                url = url + "&";
            }
            url = url + hm.get("name") + "=" + hm.get("value");
        }

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        builder.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Window.alert("ERRORE: fallita richiesta servizio (Couldn't connect to server)");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        retval.setHTML(response.getText().toString());
                    } else {
                        Window.alert("ERRORE: la risposta del servizio non è quella aspettata");
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert("ERRORE: fallita richiesta servizio (Couldn't connect to server)");
        }
        return retval;
    }

    public void generateDifferenziazione() {

        VerticalPanel retval = new VerticalPanel();

        if (documentsListReady) {
            title.setText("Differenziazione");
            content = new Label("Differenziazione");
        } else {
            title.setText("Non ancora disponibile");
            content = new Label("Differenziazione non pronta");

        }

        //return retval;

    }

    public Button createDifferenziazioneButton(final Panel service, final String path) {
        Button retval = new Button("Differenziazione su base ontologica");
        retval.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                service.clear();
                service.setVisible(true);
                final ArrayList<RadioButton> rblist = new ArrayList<RadioButton>();

                final Button closeButton = new Button("Chiudi il servizio");
                closeButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        service.clear();
                        service.setVisible(false);
                    }
                });

                if (path.contains("l_uovo") || path.contains("le_visioni") || path.contains("nascosti") || path.contains("paul_new")) {
                    service.add(new Label("Seleziona il file del quale ti interessa vedere la differenza su base ontologica rispetto al file attuale:"));
                    for (int i = 0; i < documents.size(); i++) {
                        String actual = (String) documents.get(i).get("path");
                        if (actual.substring(0, 18).equalsIgnoreCase(path.substring(0, 18)) && !path.equalsIgnoreCase(actual)) {
                            RadioButton rb = new RadioButton("diff", actual);
                            rblist.add(rb);
                            service.add(rb);
                        }
                    }
                    Button send = new Button("Visualizza differenze");
                    send.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            service.clear();
                            ArrayList<HashMap> params = new ArrayList<HashMap>();

                            HashMap pathMap1 = new HashMap();
                            pathMap1.put("name", "path1");
                            pathMap1.put("value", path);

                            String path2 = "";
                            for (int i = 0; i < rblist.size(); i++) {
                                if (rblist.get(i).getValue()) {
                                    path2 = rblist.get(i).getText();
                                }
                            }
                            HashMap pathMap2 = new HashMap();
                            pathMap2.put("name", "path2");
                            pathMap2.put("value", path2); //TODO radiobuttonvalue

                            HashMap serviceMap = new HashMap();
                            serviceMap.put("name", "service");
                            serviceMap.put("value", "Differenziazione");

                            params.add(pathMap1);
                            params.add(pathMap2);
                            params.add(serviceMap);

                            service.add(makeDispatcherRequest(params));
                            service.add(closeButton);
                        }
                    });
                    service.add(send);


                } else {
                    service.add(new Label("Servizio non disponibile per questo documento."));
                }

                service.add(closeButton);
            }
        });

        return retval;

    }

    public void createPages() {
        pageHomepage(homepage);
        pageDocumentsViewer(documentsViewer);
        pageHelp(help);
        pageContacts(contacts);
    }

    public void pageHomepage(VerticalPanel panel) {
        panel.setWidth("100%");

        Label title = new Label("Homepage");
        title.addStyleName("pageTitle");
        panel.add(title);

        panel.add(new Label("siamo adsfjaskfj asdfijasdfi jasifjas idjfiasjfiasjf"));
    }

    public void pageDocumentsViewer(VerticalPanel panel) {
        panel.setWidth("100%");

        Label title = new Label("Visualizzatore documenti TEI");
        title.addStyleName("pageTitle");
        panel.add(title);

        documentViewerPanel.setSize("100%", "100%");
        panel.add(documentViewerPanel);
    }

    public void pageHelp(VerticalPanel panel) {
        panel.setWidth("100%");

        Label title = new Label("Help");
        title.addStyleName("pageTitle");
        panel.add(title);

        panel.add(new Label("ti spiego siamo adsfjaskfj asdfijasdfi jasifjas idjfiasjfiasjf"));
    }

    public void pageContacts(VerticalPanel panel) {
        panel.setWidth("100%");

        Label title = new Label("Contatti");
        title.addStyleName("pageTitle");
        panel.add(title);

        panel.add(new Label("chiamaci siamo adsfjaskfj asdfijasdfi jasifjas idjfiasjfiasjf"));
    }
}
