package se.metria.markkoll.vptestdatagenerator;

public class VpTestdataGeneratorApplication {
    public static void main(String[] args) throws Exception {
        var gen = new VpTestdataGenerator();

        var in_path = args[0];
        var out_path = args[1];

        gen.generate(in_path, out_path);

        System.out.println("Genererade värderingsprotokoll på sökväg: " + out_path);
    }
}
