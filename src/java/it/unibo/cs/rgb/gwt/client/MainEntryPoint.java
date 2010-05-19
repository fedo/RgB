/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.gwt.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

    TabPanel docviewPanel = new TabPanel();
    final ArrayList<HashMap> doclist = new ArrayList<HashMap>();
    HorizontalPanel debug = new HorizontalPanel();
    String strglobal = "sono una stringa global";
    
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

        /*final Label fromrpc = new Label();
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                //display error text if we can't get the quote:
                Window.alert("Failed RPCServiceTei");
            }

            public void onSuccess(Object result) {
                //display the retrieved quote in the label:
                String stringa = ((ArrayList<String>) result).get(0);
                fromrpc.setText(stringa);
            }
        };
        getService().getTeiInfo(callback);

        return fromrpc;*/
        return new Label("Header [Branding]: logo + [Servizi stabili]: link help, link contatti");
    }

    protected Widget createFooterWidget() {
        //return new Label("Footer [?]");
        debug.add(new Label("Debug:"));
        return debug;
    }

    protected Widget createWestWidget() {

        final VerticalPanel west = new VerticalPanel();
        Label title = new Label("West");

        west.add(title);
        //west.add(createDoclistWidget());
        //west.add(createDoclistWidgetRPC());

        String url = "http://localhost:8080/RgB/Tei";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        final HTML tmp = new HTML();
        final String tmpresponse = "";

        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    // Couldn't connect to server (could be timeout, SOP violation, etc.)
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
                                    int index = docviewPanel.getWidgetCount() - 1;

                                    // cerco il tab
                                    while (index >= 0) {
                                        if (docviewPanel.getWidget(index).getTitle().equalsIgnoreCase(teiname)) {
                                            docviewPanel.selectTab(index);
                                            break;
                                        }
                                        index--;

                                    }

                                    // tab non esiste
                                    if (index < 0) {

                                        // contenuto del tab
                                        final Widget tabPanel = createDocviewTab(teiname);

                                        // tabText
                                        HorizontalPanel tabText = new HorizontalPanel();

                                        //TODO: visualizzare un tabText "carino"
                                        Label tabTitle = new Label("Tab " + teiname);
                                        tabText.add(tabTitle);

                                        // X che chiude il tab
                                        ClickHandler xclose = new ClickHandler() {

                                            public void onClick(ClickEvent event) {
                                                docviewPanel.remove(tabPanel);
                                            }
                                        };
                                        Label x = new Label("X");
                                        x.addClickHandler(xclose);
                                        tabText.add(x);

                                        docviewPanel.add(tabPanel, tabText);
                                        docviewPanel.selectTab(docviewPanel.getWidgetCount() - 1);
                                    }
                                }
                            });


                            west.add(currentWidget);

                        }





                    } else {
                        // Handle the error.  Can get the status text from response.getStatusText()
                    }
                }
            });
        } catch (RequestException e) {
            // Couldn't connect to server
        }

        //crezione menu

        //String xml = "<element att=\"some attribute\">some text</element>";
        //Document doc = XMLParser.parse(tmpresponse);
        //Document tmpparsato = XMLParser.parse(tmpresponse);

        // west.add(new Label("figli "+tmpparsato.getFirstChild().getNodeValue().toString()));




        return west;
    }

    protected Widget createContentWidget() {
        /*String text1 = "Lorem ipsum dolor sit amet...";
        String text2 = "Sed egestas, arcu nec accumsan...";
        String text3 = "Proin tristique, elit at blandit...";

        
        FlowPanel flowpanel;

        flowpanel = new FlowPanel();
        flowpanel.add(new Label(text1));
        docviewPanel.add(flowpanel, "One");

        docviewPanel.selectTab(0);*/

        docviewPanel.setSize("100%", "250px");
        docviewPanel.addStyleName("table-center");

        return docviewPanel;
        //return new Label("Content");
    }

    protected Widget createDocviewTab(String teiapath) {
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
    }
}