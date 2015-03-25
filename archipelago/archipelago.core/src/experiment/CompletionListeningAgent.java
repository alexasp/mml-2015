package experiment;

import jade.core.Agent;

import java.util.function.Consumer;

/**
 * Created by aspis on 25.03.2015.
 */
public class CompletionListeningAgent extends Agent {
    private final Consumer<Experiment> _completionAction;

    public CompletionListeningAgent(Consumer<Experiment> completionAction) {
        _completionAction = completionAction;
    }
}
