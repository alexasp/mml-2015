package privacy;

import communication.PeerAgent;
import learning.LabeledSample;
import privacy.math.NoiseGenerator;

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class NoisyQueryableFactory {
    private NoiseGenerator _noiseGenerator;

    public <T> NoisyQueryable<T> getQueryable(Budget budget, List<T> data) {
        return NoisyQueryable.getQueryable(budget, data, _noiseGenerator);
    }
}
