package compiler.logc;

import compiler.CeParseUtils;

public class Main {

    public static void main(String[] args) throws Exception {
        String cupfile = "src/compiler/logc/c_parser.cup";

        CeParseUtils.parseCup("CParser", "CSym", cupfile);

    }
}
