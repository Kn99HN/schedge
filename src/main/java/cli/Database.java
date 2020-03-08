package cli;

import actions.CleanData;
import actions.ScrapeTerm;
import actions.UpdateData;
import api.App;
import cli.templates.OutputFileMixin;
import cli.templates.SubjectCodeMixin;
import cli.templates.TermMixin;
import database.GetConnection;
import api.v1.SelectCourses;
import database.epochs.CleanEpoch;
import database.epochs.LatestCompleteEpoch;
import database.instructors.UpdateInstructors;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import nyu.SubjectCode;
import nyu.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "db", synopsisSubcommandLabel =
                                      "(scrape | query | update | serve)")
public class Database implements Runnable {
  @CommandLine.Spec private CommandLine.Model.CommandSpec spec;

  private static Logger logger = LoggerFactory.getLogger("cli.Database");
  private static ProgressBarBuilder barBuilder =
      new ProgressBarBuilder()
          .setStyle(ProgressBarStyle.ASCII)
          .setConsumer(new ConsoleProgressBarConsumer(System.out));

  @Override
  public void run() {
    throw new CommandLine.ParameterException(spec.commandLine(),
                                             "Missing required subcommand");
  }

  @CommandLine.Command(
      name = "scrape", sortOptions = false, headerHeading = "Usage:%n%n",
      synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
      parameterListHeading = "%nParameters:%n",
      optionListHeading = "%nOptions:%n", header = "Scrape section from db",
      description =
          "Scrape section based on term and registration number, OR school and subject from db")
  public void
  scrape(@CommandLine.Mixin TermMixin termMixin,
         @CommandLine.
         Option(names = "--batch-size-catalog",
                description = "batch size for querying the catalog")
         Integer batchSize,
         @CommandLine.Option(names = "--batch-size-sections",
                             description = "batch size for querying sections")
         Integer batchSizeSections) {
    long start = System.nanoTime();
    ScrapeTerm.scrapeTerm(
        termMixin.getTerm(), batchSize, batchSizeSections,
        subjectCodes -> ProgressBar.wrap(subjectCodes, barBuilder));
    long end = System.nanoTime();
    logger.info((end - start) / 1000000000 + " seconds");
  }

  @CommandLine.
  Command(name = "rmp", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Scrape section from db",
          description = "Update instructors using RMP")
  public void
  rmp(@CommandLine.Option(names = "--batch-size",
                          description = "batch size for querying RMP")
      Integer batchSize) {
    long start = System.nanoTime();
    GetConnection.withContext(context -> {
      List<SubjectCode> allSubjects = SubjectCode.allSubjects();
      UpdateInstructors.updateInstructors(
          context,
          ProgressBar.wrap(UpdateInstructors.instructorUpdateList(context),
                           barBuilder),
          batchSize);
    }

    );

    long end = System.nanoTime();
    logger.info((end - start) / 1000000000 + " seconds");
  }

  @CommandLine.Command(
      name = "query", sortOptions = false, headerHeading = "Usage:%n%n",
      synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
      parameterListHeading = "%nParameters:%n",
      optionListHeading = "%nOptions:%n", header = "Query section",
      description =
          "Query section based on term and registration number, OR school and subject from db")
  public void
  query(@CommandLine.Mixin TermMixin termMixin,
        @CommandLine.Mixin SubjectCodeMixin subjectCodeMixin,
        @CommandLine.Mixin OutputFileMixin outputFile) {
    long start = System.nanoTime();
    GetConnection.withContext(context -> {
      Term term = termMixin.getTerm();
      Integer epoch = LatestCompleteEpoch.getLatestEpoch(context, term);
      if (epoch == null) {
        logger.warn("No completed epoch for term=" + term);
        return;
      }
      outputFile.writeOutput(SelectCourses.selectCourses(
          context, epoch, subjectCodeMixin.getSubjectCodes()));
    });

    long end = System.nanoTime();
    double duration = (end - start) / 1000000000.0;
    logger.info(duration + " seconds");
  }

  @CommandLine.
  Command(name = "clean", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Serve data",
          description = "Clean epochs")
  public void
  clean(@CommandLine.Mixin TermMixin termMixin,
        @CommandLine.Option(names = "--epoch",
                            description = "The epoch to clean") Integer epoch) {
    Term term = termMixin.getTermAllowNull();
    GetConnection.withContext(context -> {
      if (epoch == null && term == null) {
        logger.info("Cleaning incomplete epochs...");
        CleanEpoch.cleanIncompleteEpochs(context);
      } else if (epoch != null && term == null) {
        logger.info("Cleaning epoch={}...", epoch);
        CleanEpoch.cleanEpoch(context, epoch);
      } else if (term != null && epoch == null) {
        logger.info("Cleaning epochs for term={}...", term);
        CleanEpoch.cleanEpochs(context, term);
      } else {
        throw new CommandLine.ParameterException(
            spec.commandLine(), "Term and --epoch are mutually exclusive!");
      }
    });
  }

  @CommandLine.
  Command(name = "serve", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Serve data",
          description = "Serve data through an API")
  public void
  serve() {
    GetConnection.initIfNecessary();
    App.run();
    while (true) {
      CleanData.cleanData();
      UpdateData.updateData();

      try {
        TimeUnit.DAYS.sleep(1);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
