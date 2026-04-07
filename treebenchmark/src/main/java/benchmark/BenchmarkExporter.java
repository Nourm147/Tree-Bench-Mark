package benchmark;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import Input.InputDistribution;

public class BenchmarkExporter {

    private static final String HEADER
            = "distribution,label,mean_ms,median_ms,stddev_ms,tree_height";

    private final PrintWriter writer;

    public BenchmarkExporter(Path outputPath) throws IOException {
        Files.createDirectories(outputPath.getParent());
        this.writer = new PrintWriter(Files.newBufferedWriter(outputPath));
        writer.println(HEADER);
    }

    public void appendRow(InputDistribution dist, BenchmarkData data) {
        writer.printf("%s,%s,%.4f,%.4f,%.4f,%d%n",
                dist.name(),
                data.getLabel().trim(),
                data.getMean() / 1e6,
                data.getMedian() / 1e6,
                data.getStdDev() / 1e6,
                data.getTreeHeight());
    }

    public void close() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
