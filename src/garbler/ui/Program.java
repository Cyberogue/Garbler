/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, mergeValues, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package garbler.ui;

import garbler.structure.BasicDecimalCharMap;
import garbler.library.*;
import garbler.builder.*;
import garbler.structure.BasicIntegerCharMap;
import java.util.Map.Entry;

/**
 * Main test class simply used for running individual test cases
 *
 * @author Rogue <Alice Q.>
 */
public class Program {

    // MAIN TEST METHOD
    // THIS WILL BE TAKEN OUT OF THE FINAL VERSION AND IS JUST FOR TESTING TEST CASES
    public static void main(String[] args) {
        StatsLibrary lib = new StatsLibrary(false);
        StatsCruncher sc = new StatsCruncher(lib);
        WordBuilder builder = new WordBuilder(sc);

        /*
         // LATIN/LOREM IPSUM
         String testSeed = "Lorem ipsum dolor sit amet, quando exerci at mel. Ea duo wisi iusto, ad scribentur comprehensam nec, vel ea everti detracto offendit. Ius agam ancillae accusata in, duo congue iriure ex, ne debitis mentitum usu. Ut movet sensibus aliquando sed. Cum ignota facilis cu, case natum iisque eu quo, timeam dolores vulputate mei ex. Nihil soluta commodo nec ut.\n"
         + "Erat consectetuer comprehensam vim ad, et mei dolore aliquid officiis. Clita eleifend at pri, sale detraxit ea quo, qui dolorum fabellas ad. Dico nonumes deseruisse mei cu. Ludus inimicus temporibus per ne, pro meis dolores oporteat et.\n"
         + "Mel lorem bonorum cu, in duo nemore necessitatibus. Mei vitae utinam cu. Natum dicunt placerat sed et, ut usu eius repudiandae. Ad sale audiam consectetuer his, no mel omnium complectitur. Iusto nostrum luptatum pri at, nam odio eripuit necessitatibus no.\n"
         + "Et quo elitr fuisset. Vim cu cetero laoreet cotidieque, vis ei noluisse persequeris reformidans. Vel te aeque appareat placerat. Mea ea tale indoctum temporibus, in quo repudiare cotidieque, magna habemus nam ei. Vix ei etiam fabellas, ei paulo nonumy vel. Ea adolescens voluptaria sit.\n"
         + "Eum no vitae consul option. Cu labores utroque similique sit, sed admodum constituto et. Ad per debet erant reprehendunt, ne vim reque habemus. Oratio ubique dignissim no est, quis graeco corrumpit ne eam.\n"
         + "Inimicus honestatis intellegebat ex eum, quas percipit mei ne. Insolens instructior definitiones ne vix, ius id quem novum putant, populo intellegebat vix id. Qui ut zril viderer eloquentiam. Duo nulla nihil conclusionemque no, pro id etiam doming. An sonet scripta delicata sed. Eu qui eleifend principes, cu eum civibus euripidis liberavisse. Vix at viderer epicuri, cu agam tollit complectitur mel.\n"
         + "Per no suas placerat, graece patrioque his at, cum possim deleniti id. Vim quot malis eleifend ne, per at nulla possit invidunt, sea virtute platonem ex. An sit populo labitur, qui duis appareat cu, adhuc inani an vix. Erant tation fabellas no quo. At mel ignota platonem interesset, eos vitae disputando ea. Ut ludus ignota reprehendunt usu, eos no odio ferri, legere iuvaret cum ea.\n"
         + "Te saepe quaestio per. Ea ullum alterum percipitur duo, tale augue necessitatibus ius ex, mea latine bonorum antiopam at. Viris choro ceteros pri ex, et vis dico tacimates iudicabit. Usu an nonumy impedit, mei at eros fabellas. Quo te detraxit periculis reprehendunt.\n"
         + "Cu sea solet eleifend conceptam, dicit melius delectus qui at. Cu dicta nostro antiopam nam, id eum nisl eros causae. Et ius graeci omnesque, ea est commune ullamcorper. Vim porro etiam debitis id, usu apeirian atomorum tincidunt cu, qui saepe tempor an.\n"
         + "Nec ea tollit temporibus consequuntur, vim partem aperiri theophrastus id. No est persius scribentur mediocritatem, primis saperet efficiantur quo id. Ne vel malis dolor habemus. Vel quaerendum instructior ne, pro nominavi periculis dignissim ne, ut voluptatum efficiendi liberavisse cum. Suas molestiae eam te, at laudem meliore antiopam cum.";
         */
        // SPANISH
        String testSeed = "Cocineros medievales fueron restringidos a menudo en formas fueron capaces de utilizar, tener acceso restringido a hornos "
                + "debido a los costes de construcción y necesidad de abundantes suministros de combustible de cocina. Pies podrían ser fácilmente "
                + "cocinadas sobre un fuego abierto, al asociarse con un panadero les permitió cocinar el relleno dentro de su propia carcasa definida "
                + "localmente. Las primeras recetas de circular como refieren a coffyns (la palabra realmente usada para una canasta o caja), con rectas"
                + " lados cerrados y un top; abrir mejores pies fueron denominados trampas. Esto también puede ser la razón por qué principios recetas "
                + "centran en el relleno sobre el caso que lo rodea, con la desarrollo de asociación hacia el uso de casos de pastel de barro"
                + "reutilizables que redujo el uso de harina de caro.";

        lib.parseLineSimple(testSeed, ",.");
        sc.recalculateMetrics();

        System.out.println("FREE GENERATION");
        for (int y = 0; y < 20; y++) {
            System.out.print("\t");
            for (int x = 0; x < 10; x++) {
                System.out.print(builder.generateWord(10, 0.1f) + " ");
            }
            System.out.println();
        }

        System.out.println("OTHER");
        System.out.println("\tSecondary Cache: " + sc.getSecondaryCacheContents() + "\tPrimary Cache: " + sc.getPrimaryCacheContents());
        System.out.println("\tAlphabet: " + lib.getAlphabet());
        System.out.println("\tWord Lengths: " + lib.getWordLengths());
        System.out.println("\tPrimary Counts:" + sc.getPrimaryCharacterDitribution());
    }
}
