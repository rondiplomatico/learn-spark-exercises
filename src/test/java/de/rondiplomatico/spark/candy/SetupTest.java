package de.rondiplomatico.spark.candy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.spark.sql.Encoders;
import org.junit.After;
import org.junit.Test;

import de.rondiplomatico.spark.candy.base.SparkBase;

public class SetupTest extends SparkBase {

    @Test
    public void testFileIO() {
        int n = 10000;
        Random r = new Random();
        List<Integer> data = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            data.add(r.nextInt());
        }

        getSparkSession().createDataFrame(getJavaSparkContext().parallelize(data), Integer.class)
                         .write()
                         .parquet("testout");

        List<Integer> res = getSparkSession().read()
                                             .parquet("testout")
                                             .as(Encoders.INT())
                                             .collectAsList();

        assertEquals("Written data could not be read and is equal", data, res);
    }

    @After
    public void cleanUp() throws IOException {
        Files.delete(new File("testout").toPath());
    }

}
