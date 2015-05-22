package application.experiments;

/**
 * Created by alex on 5/11/15.
 */
public class PrivacyParam {
    public final double epsilon;
    public final double perUpdateBudget;

    public PrivacyParam(double epsilon, double perUpdateBudget) {
        this.epsilon = epsilon;
        this.perUpdateBudget = perUpdateBudget;
    }

    public PrivacyParam(double epsilon) {
        this(epsilon, epsilon);
    }

    public static PrivacyParam get(double epsilon, double perUpdateBudget) {
        return new PrivacyParam(epsilon, perUpdateBudget);
    }
}
