/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.http.client.*;
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
    final VerticalPanel west = new VerticalPanel();
    HorizontalPanel debug = new HorizontalPanel();
    //variabili
    final ArrayList<HashMap> doclist = new ArrayList<HashMap>();

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

        //Log.setCurrentLogLevel(Log.getLowestLogLevel());
        //Log.setUncaughtExceptionHandler();
        //Log.info("msg");
        DockPanel mainPanel = new DockPanel();

        mainPanel.setBorderWidth(5);
        mainPanel.setSize("100%", "100%");
        mainPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
        mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        Widget header = createHeaderWidget();
        mainPanel.add(header, DockPanel.NORTH);
        mainPanel.setCellHeight(header, "30px");

        Widget footer = createFooterWidget();
        mainPanel.add(footer, DockPanel.SOUTH);
        mainPanel.setCellHeight(footer, "25px");

        Widget west = createWestWidget();
        mainPanel.add(west, DockPanel.WEST);
        mainPanel.setCellWidth(west, "150px");

        Widget content = createContentWidget();
        mainPanel.add(content, DockPanel.CENTER);
        RootPanel.get().add(mainPanel);
    }

    protected Widget createHeaderWidget() {

        Widget header = new Label("Header [Branding]: logo + [Servizi stabili]: link help, link contatti");

        header.setHeight("100px");

        return header;
    }

    protected Widget createFooterWidget() {

        Label footer = new Label();

        debug.add(new Label("Debug:"));

        return debug;
    }

    protected Widget createWestWidget() {


        Label title = new Label("West");

        west.add(title);

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

                        int lenght = new Integer(responseXml.getElementById("numberOfDocuments").getFirstChild().toString());
                        //west.add(new Label("numero documenti: "+lenght));

                        for (int i = 0; i < lenght; i++) {
                            HashMap documentInfo = new HashMap();
                            String current = responseXml.getElementsByTagName("li").item(i).toString();
                            Document currentXml = XMLParser.parse(current);
                            Label currentWidget;

                            documentInfo.put("teiname", new HTML(currentXml.getElementById("teiname").getFirstChild().toString()).getHTML());

                            final String teiname = (String) documentInfo.get("teiname");
                            currentWidget = new Label(teiname);
                            currentWidget.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {
                                    int index = documentViewPanel.getWidgetCount() - 1;

                                    // cerco il tab
                                    while (index >= 0) {
                                        if (documentViewPanel.getWidget(index).getTitle().equalsIgnoreCase(teiname)) {
                                            documentViewPanel.selectTab(index);
                                            break;
                                        }
                                        index--;

                                    }

                                    // tab non esiste
                                    if (index < 0) {

                                        // contenuto del tab
                                        final Widget tabPanel = createDocviewTab(teiname);
                                        tabPanel.setHeight("15px");

                                        // tabText
                                        HorizontalPanel tabText = new HorizontalPanel();

                                        //TODO: visualizzare un tabText "carino"
                                        Label tabTitle = new Label("Tab " + teiname);
                                        tabText.add(tabTitle);

                                        // X che chiude il tab
                                        ClickHandler xclose = new ClickHandler() {

                                            public void onClick(ClickEvent event) {
                                                documentViewPanel.remove(tabPanel);
                                            }
                                        };
                                        Label x = new Label("X");
                                        x.addClickHandler(xclose);
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
        return west;
    }

    protected Widget createContentWidget() {

        documentViewPanel.setSize("100%", "250px");
        documentViewPanel.addStyleName("table-center");

        return documentViewPanel;
    }

    protected Widget createDocviewTab(String teiapath) {

        VerticalPanel panel = new VerticalPanel();
        return panel;

        //Contesto: nome breve del documento + elenco delle sigle delle lezioni varianti disponibili + barra di comandi

        //nome breve
        //lezioni varianti disponibili
        //comandi
        //

        /*String url = "http://localhost:8080/RgB/Tei";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void onError(Request request, Throwable exception) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        } catch (RequestException ex) {
            Logger.getLogger(MainEntryPoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        return panel;*/

        /*
        VerticalPanel panel = new VerticalPanel();

        panel.setTitle(teiapath); //necessario per venire selezionato e chiuso
        panel.add(new Label("Sò che sono il " + teiapath));

        // TODO: rpc che ottiene i dati "lunghi" del file aperto
        final DisclosurePanel info = new DisclosurePanel("Clicca qui per visualizzare le informazioni complete");
        info.add(new Label("dati lunghi lunghi lunghi"));
        panel.add(info);

        // visualization And Witnesses Bar
        HorizontalPanel visualizationAndWitnesses = new HorizontalPanel();
        final HorizontalPanel witnesses = new HorizontalPanel();

        // TODO: abilitazione dinamica in base al documento (alcuni testi non supportano certe visualizzazioni
        // TODO: aggiungere listeners che ricaricano i wit aperti in accordo con la vis. scelta
        ListBox visualization = new ListBox();
        visualization.addItem("diplomatica");
        visualization.addItem("Semi-diplomatica");
        visualization.addItem("critica");
        visualizationAndWitnesses.add(new Label("Scegli il tipo di trascrizione: "));
        visualizationAndWitnesses.add(visualization);

        // TODO: lista dei wit dinamica relativa al documento
        // TODO: listeners per le checkbox dei wit
        visualizationAndWitnesses.add(new Label("Scegli i witness da visualizzare: "));

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
    }

    
}
