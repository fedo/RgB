/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.test;

import java.util.LinkedList;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Patch;

/**
 *
 * @author fedo
 */
public class TestDiff {

    public static void main(String[] args) {

        String text1 = "L'UOVO DI COLOMBO\n\nEra il 1993. Si viaggiava in treno e per andare da Roma a Los Angeles si usavano ancora quei bestioni a reazione che restavano in cielo per la bellezza di dodici ore. Era ancora l'epoca dei grandi sarti. La politica, dopo aver visto ministri in discoteca, parlamentari a Malindi e comunisti a Capalbio, faceva la spola tra le patrie galere e la televisione, dove gli italiani vedevano morire la vecchia Repubblica. Nello sport e negli spot furoreggiava sempre tal Berlusconi, circondato da pupattole e da sottoposti vestiti come lui. Volava Senna sulla sua carcassa, mentre a Mosca non si sapeva ancora che pesci prendere. Tra Nord e Sud erano ancora rose e fiori.\n\nBei tempi passati, pieni di vitalità, d'avventura, di grandi sconvolgimenti della storia. L'Italia, soffocata da una intera catena montuosa di debiti e di truffe allo Stato, faceva parte dei sette, otto paesi più ricchi del mondo. Dalle frontiere sfondate entravano frotte di disperati di tutti i colori. S'accampavano dove potevano mentre accanto a loro, a tutta velocità, sfrecciavano le Ferrari dei nuovi ricchi. I figli non andavano di moda, nessuno li faceva più: il piacere del denaro aveva svuotato le case. Si riempivano solo la sera davanti alle immagini pallide e un po' disfatte della televisione (non c'era ancora neanche l'alta definizione). Le donne lavoravano quasi tutte e gli uomini avevano ognuno due, tre lavori. Solo i vecchi se ne restavano fra le quattro mura e d'estate ne morivano a grappoli sulle panchine sotto casa. Giapponesi, americani, tedeschi, australiani scorrazzavano in mutande da Palermo a Venezia e con le loro ciabattelle solcavano le antiche strade romane e medievali.\n\nBene, in quell'Italia ancora felice e rubiconda viveva un omino appartato e silenzioso. Nessuno lo conosceva, nessuno si curava di lui. Per strada lo urtavano tutti perché non lo vedevano. Si chiamava Demetrio Trivelli. Sì, proprio lui. ";
        String text2 = "L'UOVO DI COLOMBO di Vincenzo Cerami\n\nEra il 1991. Si viaggiava in treno e per andare da Roma a Los Angeles si usavano ancora quei bestioni a reazione che restavano in cielo per la bellezze di dodici ore. Continuavano a furoreggiare vecchie cariatidi come Mike Bongiorno e Pippo Baudo, dei fantasmi venuti dal bianco e nero. Era l'epoca dei grandi sarti: Valentino, Armani, Ferré. La politica vedeva ministri in discoteca, parlamentari a Malindi e comunisti a Capalbio. Nello sport e negli spot furoreggiava Berlusconi, sempre circondato da belle pupattole e da sottoposti vestiti come lui. Volava Senna sulla sua carcassa, Maradona era da poco scappato in Argentina. La mafia continuava ad ammazzare i giudici dopo che i terroristi, mentre a Mosca i falchi del vecchio regime tentavano il colpo di Stato. Lo scrittore Salvalaggio e il critico teatrale Raboni erano conosciuti solo da noi. Mentre si idolatrava (un premio a chi ne ha mai sentito parlare) Italo Calvino. Intanto nei nostri mari tornarono a fiorirevano alghe e schiume, così prezise oggi alle nostre industrie chimiche e farmaceutiche.\n\nBei tempi passati, pieni di vitalità, d'avventura, di grandi sconvolgimenti della storia. L'italia, soffocata da una intera catena montuosa di debiti, faceva parte dei sette Paesi più ricchi del mondo. Dalle frontiere sfondate entravano frotte di disperati di tutti i colori. S'accampavano dove potevano mentre accanto, a tutta velocità, sfrecciavano le Ferrari dei nuovi ricchi. I figli non andavano di moda, nessuno li faceva più: il piacere del danaro aveva svuotate le case, Si riempivano solo la sera per vedere quelle immagini pallide e un po' disfatte della televisione (non c'era neanche l'alta definizione). Le donne lavoravano quasi tutte e gli uomini avevano ognuno due, tre lavori. Solo i vecchi se ne restavano fra le quattro mura di casa e d'estate ne morivano a grappoli sulle panchine sotto casa. Giapponesi, americani, tedeschi, australiani scorrazzavano in mutande da Palermo a Venezia e con le loro ciabattelle solcavano le antiche strade romane e medievali. Il 1991 fu l'anno della Sampdoria e di Fontana di Trevi, riportata a lucido per la gioia della Kodak.\n\nBene, in quell'Italia felice e rubiconda viveva un omino appartato e silenzioso. Nessuno lo conosceva, nessuno si curava di lui. Per strada lo urtavano tutti perché non lo vedevano. Si chiamava Demetrio Travello. Sì, proprio lui. ";

        diff_match_patch diff = new diff_match_patch();
        LinkedList<Diff> list = diff.diff_main(text1, text2);
        diff.diff_cleanupSemantic(list);
        String output = diff.diff_prettyHtml(list);
        System.out.println(output);

        diff.diff_cleanupMerge(list);
        LinkedList<Patch> listp = diff.patch_make(list);
        output = diff.patch_toText(listp);
        System.out.println(output);

    }
}
