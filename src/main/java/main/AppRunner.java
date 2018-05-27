package main;

import java.io.IOException;

public class AppRunner {

    public static void main(String[] args) throws IOException {
        CLIHandler cliHandler = new CLIHandler();
        InputArgs inputArgs = cliHandler.processInputArgs(args);

        if (inputArgs.getDataset().equals("iris")) {
            MainIris.setInputArgs(inputArgs);
            MainIris.main(args);
        }
        if (inputArgs.getDataset().equals("reuters")) {
            MainReuters.setInputArgs(inputArgs);
            MainReuters.main(args);
        }
        if (inputArgs.getDataset().equals("news")) {
            MainNews.setInputArgs(inputArgs);
            MainNews.main(args);
        }
    }
}
