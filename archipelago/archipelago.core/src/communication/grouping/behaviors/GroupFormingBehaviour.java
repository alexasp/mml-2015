package communication.grouping.behaviors;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by alex on 4/23/15.
 */
public class GroupFormingBehaviour extends CyclicBehaviour {
    private final int _iterations;
    private GroupFormingBehaviour _groupFormingBehaviour;

    public GroupFormingBehaviour(int iterations) {
        _iterations = iterations;
    }

    @Override
    public void action() {
    }

}
