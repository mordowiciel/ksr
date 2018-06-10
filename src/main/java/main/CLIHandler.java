package main;

import static java.lang.System.exit;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class CLIHandler {

    private static final String NEIGHBOURS_PARAM = "k";
    private static final String NEIGHBOURS_PARAM_NAME = "k_neighbours";
    private static final String NEIGHBOURS_PARAM_DESC = "K neighbours";

    private static final String DISTANCE_PARAM = "d";
    private static final String DISTANCE_PARAM_NAME = "distance";
    private static final String DISTANCE_PARAM_DESC = "Distance calculator";

    private static final String FEATURE_EXTRACTOR_PARAM = "f";
    private static final String FEATURE_EXTRACTOR_PARAM_NAME = "feature_extractor";
    private static final String FEATURE_EXTRACTOR_PARAM_DESC = "Feature extractor";

    private static final String DATASET_PARAM = "ds";
    private static final String DATASET_PARAM_NAME = "dataset";
    private static final String DATASET_PARAM_DESC = "Dataset to choose";

    private static final String DATASET_PATH_PARAM = "p";
    private static final String DATASET_PATH_PARAM_NAME = "dataset-path";
    private static final String DATASET_PATH_PARAM_DESC = "Path leading to the dataset files directory";

    private static final String LABELS_PARAM = "lb";
    private static final String LABELS_PARAM_NAME = "labels";
    private static final String LABELS_PARAM_DESC = "Labels to perform classification on";

    private static final String VECTOR_SIZE_PARAM = "v";
    private static final String VECTOR_SIZE_PARAM_NAME = "vector-size";
    private static final String VECTOR_SIZE_PARAM_DESC = "Vector size threshold";

    private static final String HELP_PARAM = "h";
    private static final String HELP_PARAM_NAME = "help";
    private static final String HELP_PARAM_DESC = "Print this dialog.";
    private static final String HELP_USAGE = "KSR Zad 1";

    private static final String LOGGER_PATTERN_LAYOUT = "%d %-5p [%c{1}] %m%n";

    private static final Logger LOG = Logger.getLogger("app.initialization");

    private Options options;

    public CLIHandler() {
        initOptions();
    }

    public InputArgs processInputArgs(String[] args) {

        InputArgs inputArgs = new InputArgs();

        try {

            CommandLine cmd = new DefaultParser().parse(options, args);

            if (isHelpOptionProvided(cmd)) {
                printHelp();
                exit(0);
            }

            areRequiredOptionsProvided(cmd);

            if (isOptionProvided(cmd, NEIGHBOURS_PARAM_NAME)) {
                String neighbours = cmd.getOptionValue(NEIGHBOURS_PARAM_NAME);
                inputArgs.setNeighbours(Integer.valueOf(neighbours));
            }

            if (isOptionProvided(cmd, DISTANCE_PARAM_NAME)) {
                String distance = cmd.getOptionValue(DISTANCE_PARAM_NAME);
                inputArgs.setDistance(distance);
            }

            if (isOptionProvided(cmd, FEATURE_EXTRACTOR_PARAM_NAME)) {
                String featureExtractor = cmd.getOptionValue(FEATURE_EXTRACTOR_PARAM_NAME);
                inputArgs.setFeatureExtractor(featureExtractor);
            }

            if (isOptionProvided(cmd, LABELS_PARAM_NAME)) {
                String labels = cmd.getOptionValue(LABELS_PARAM_NAME);
                inputArgs.setLabels(labels);
            }

            if (isOptionProvided(cmd, DATASET_PARAM_NAME)) {
                String dataset = cmd.getOptionValue(DATASET_PARAM_NAME);
                inputArgs.setDataset(dataset);
            }

            if (isOptionProvided(cmd, DATASET_PATH_PARAM_NAME)) {
                String datasetPath = cmd.getOptionValue(DATASET_PATH_PARAM_NAME);
                inputArgs.setDatasetPath(datasetPath);
            }

            if (isOptionProvided(cmd, VECTOR_SIZE_PARAM_NAME)) {
                String vectorSize = cmd.getOptionValue(VECTOR_SIZE_PARAM_NAME);
                inputArgs.setVectorSize(Integer.valueOf(vectorSize));
            } else {
                inputArgs.setVectorSize(Integer.MAX_VALUE);
            }

            addLoggerFileAppender(inputArgs, Level.INFO);

        } catch (MissingOptionException moe) {
            System.err.println(moe);
            printHelp();
        } catch (MissingArgumentException mae) {
            System.err.println(mae);
            printHelp();
        } catch (ParseException pe) {
            System.err.println(pe);

        } catch (IOException ioe) {
            System.err.println(ioe);
        }

        return inputArgs;
    }

    private void addLoggerFileAppender(InputArgs inputArgs, Level threshold) throws IOException {

        String logFilename;

        if (inputArgs.getFeatureExtractor() == null) {
            logFilename = inputArgs.getDataset() + "_" + "k" + inputArgs.getNeighbours() + "_"
                    + inputArgs.getDistance() + "_" + inputArgs.getLabels() + inputArgs.getVectorSize()
                    + "_" + "vs" + inputArgs.getVectorSize();
            ;
        } else {
            logFilename = inputArgs.getDataset() + "_" + "k" + inputArgs.getNeighbours() + "_"
                    + inputArgs.getDistance() + "_" + inputArgs.getFeatureExtractor() + "_" + inputArgs.getLabels()
                    + "_" + "vs" + inputArgs.getVectorSize();
        }

        String logPath = inputArgs.getDataset() + "/" +
                inputArgs.getFeatureExtractor() + "/" +
                inputArgs.getLabels() + "/" +
                logFilename;

        FileAppender fileAppender = new FileAppender(new PatternLayout(LOGGER_PATTERN_LAYOUT), logPath, false);
        fileAppender.setThreshold(threshold);
        Logger.getRootLogger().addAppender(fileAppender);
        LOG.info(threshold.toString() + " log will be saved at " + logPath);
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(HELP_USAGE, options);
    }

    private boolean isHelpOptionProvided(CommandLine cmd) {
        return cmd.hasOption(HELP_PARAM_NAME);
    }

    private boolean isOptionProvided(CommandLine cmd, String paramName) {
        return cmd.hasOption(paramName) && !cmd.getOptionValue(paramName).equals(EMPTY);
    }

    private void areRequiredOptionsProvided(CommandLine cmd) throws MissingOptionException {

        if (!isOptionProvided(cmd, DISTANCE_PARAM_NAME)) {
            printHelp();
            System.exit(1);
            throw new MissingOptionException("No distance provided.");
        }
        if (!isOptionProvided(cmd, NEIGHBOURS_PARAM_NAME)) {
            printHelp();
            System.exit(1);
            throw new MissingOptionException("No neighbours count provided.");
        }
        if (!isOptionProvided(cmd, DATASET_PARAM_NAME)) {
            printHelp();
            System.exit(1);
            throw new MissingOptionException("No dataset provided.");
        }
        if (!isOptionProvided(cmd, DATASET_PATH_PARAM_NAME)) {
            printHelp();
            System.exit(1);
            throw new MissingOptionException("No dataset path provided.");
        }
    }

    private void initOptions() {
        this.options = new Options()
                .addOption(Option.builder(NEIGHBOURS_PARAM)
                        .hasArg(true)
                        .argName(NEIGHBOURS_PARAM_NAME + "_path")
                        .desc(NEIGHBOURS_PARAM_DESC)
                        .longOpt(NEIGHBOURS_PARAM_NAME)
                        .build())
                .addOption(Option.builder(DISTANCE_PARAM)
                        .hasArg(true)
                        .argName(DISTANCE_PARAM_NAME + "_path")
                        .longOpt(DISTANCE_PARAM_NAME)
                        .desc(DISTANCE_PARAM_DESC)
                        .build())
                .addOption(Option.builder(FEATURE_EXTRACTOR_PARAM)
                        .hasArg(true)
                        .argName(FEATURE_EXTRACTOR_PARAM_NAME + "_path")
                        .longOpt(FEATURE_EXTRACTOR_PARAM_NAME)
                        .desc(FEATURE_EXTRACTOR_PARAM_DESC)
                        .build())
                .addOption(Option.builder(LABELS_PARAM)
                        .hasArg(true)
                        .argName(LABELS_PARAM_NAME + "_path")
                        .longOpt(LABELS_PARAM_NAME)
                        .desc(LABELS_PARAM_DESC)
                        .build())
                .addOption(Option.builder(DATASET_PARAM)
                        .hasArg(true)
                        .argName(DATASET_PARAM_NAME + "_path")
                        .longOpt(DATASET_PARAM_NAME)
                        .desc(DATASET_PARAM_DESC)
                        .build())
                .addOption(Option.builder(DATASET_PATH_PARAM)
                        .hasArg(true)
                        .argName(DATASET_PATH_PARAM_NAME + "_path")
                        .longOpt(DATASET_PATH_PARAM_NAME)
                        .desc(DATASET_PATH_PARAM_DESC)
                        .build())
                .addOption(Option.builder(VECTOR_SIZE_PARAM)
                        .hasArg(true)
                        .argName(VECTOR_SIZE_PARAM_NAME)
                        .longOpt(VECTOR_SIZE_PARAM_NAME)
                        .desc(VECTOR_SIZE_PARAM_DESC)
                        .build())
                .addOption(Option.builder(HELP_PARAM)
                        .hasArg(false)
                        .longOpt(HELP_PARAM_NAME)
                        .desc(HELP_PARAM_DESC)
                        .build());
    }
}
