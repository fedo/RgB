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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main entry point.
 *
 * @author fedo
 */
public class MainEntryPoint implements EntryPoint {

    TabPanel docviewPanel = new TabPanel();
    
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

        return new Label("Header [Branding]: logo + [Servizi stabili]: link help, link contatti");
    }

    protected Widget createFooterWidget() {
        return new Label("Footer [?]");
    }

    protected Widget createWestWidget() {

        VerticalPanel west = new VerticalPanel();
        Label title = new Label("West");

        west.add(title);
        west.add(createDoclistWidget());

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

    protected Widget createDoclistWidget() {

        //TODO: da implementare l'rpc che richiede la lista dei files e quindi la generazione dinamica
        class DoclistTree extends Composite implements TreeListener {

            Tree tree = new Tree();
            Label info = new Label("Doclist");

            public DoclistTree() {
                VerticalPanel vp = new VerticalPanel();
                vp.addStyleName("demo-table");
                vp.addStyleName("table-center");
                vp.setWidth("150px");

                TreeItem outerRoot = new TreeItem("Item 1");
                outerRoot.addItem("Item 1-1");
                outerRoot.addItem("Item 1-2");
                outerRoot.addItem("Item 1-3");
                tree.addItem(outerRoot);

                TreeItem innerRoot = new TreeItem("Item 1-5");
                innerRoot.addItem("Item 1-5-1");
                innerRoot.addItem("Item 1-5-2");
                innerRoot.addItem("Item 1-5-3");
                innerRoot.addItem("Item 1-5-4");

                outerRoot.addItem(innerRoot);
                tree.addTreeListener(this);


                vp.add(tree);
                vp.setCellHeight(tree, "201px");

                DOM.setStyleAttribute(info.getElement(),
                        "backgroundColor", "#ffc");

                DOM.setStyleAttribute(info.getElement(),
                        "borderTop", "3px solid #999");

                vp.add(info);

                initWidget(vp);
            }

            public void onTreeItemSelected(TreeItem item) {
                info.setText("Selected " + item.getText());

                /*
                 * if(item.getText() è in docviewPanel){
                 *  trovala e selezionalo}
                 * else{
                 *  creane uno nuovo e selezionalo
                 * }
                 */
                int index = docviewPanel.getWidgetCount()-1;

                // cerco il tab
                while(index >= 0){
                    if(docviewPanel.getWidget(index).getTitle().equalsIgnoreCase(item.getText())){
                        docviewPanel.selectTab(index);
                        break;
                    }
                    index--;

                }

                // tab non esiste
                if (index < 0) {

                    // contenuto del tab
                    final Widget tabPanel = createDocviewTab(item.getText());

                    // tabText
                    HorizontalPanel tabText = new HorizontalPanel();

                    //TODO: visualizzare un tabText "carino"
                    Label tabTitle = new Label("Tab " + item.getText());
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
                    docviewPanel.selectTab(docviewPanel.getWidgetCount()-1);
                }
            }

            public void onTreeItemStateChanged(TreeItem item) {
                info.setText(item.getText() + " State Changed");
            }
        }

        return new DoclistTree();
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
