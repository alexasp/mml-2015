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


}