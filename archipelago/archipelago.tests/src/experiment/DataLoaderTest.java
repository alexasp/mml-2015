package experiment;

import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DataLoaderTest {


    private DataLoader _dataLoader;

    @Before
    public void setUp(){
        _dataLoader = new DataLoader();
    }


    @Test
    public void partition_DividesData() {
        int records = 7;
        List<LabeledSample> samples = IntStream.range(0, records)
                .mapToObj(i -> mock(LabeledSample.class))
                .collect(Collectors.toList());

        List<List<LabeledSample>> partitions = _dataLoader.partition(3, samples);

        assertEquals(3, partitions.get(0).size());
        assertEquals(2, partitions.get(1).size());
        assertEquals(2, partitions.get(2).size());
    }

    @Test
    public void partition_SinglePartition_ReturnsData(){
        List<LabeledSample> samples = IntStream.range(0, 10)
                .mapToObj(i -> mock(LabeledSample.class))
                .collect(Collectors.toList());

        List<List<LabeledSample>> partitioned = _dataLoader.partition(1, samples);

        assertEquals(samples, partitioned.get(0));
        assertEquals(1, partitioned.size());
    }

    @Test
    public void partitionWithLimit_DividesDataRespectingLimit() {
        int records = 100;
        List<LabeledSample> samples = IntStream.range(0, records)
                .mapToObj(i -> mock(LabeledSample.class))
                .collect(Collectors.toList());

        List<List<LabeledSample>> partitions = _dataLoader.partition(3, samples, 20);

        assertEquals(20, partitions.get(0).size());
        assertEquals(20, partitions.get(1).size());
        assertEquals(20, partitions.get(2).size());
    }

}