package application.experiments;

import application.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.Experiment;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args){

        Injector injector = Guice.createInjector(new AppInjector());
    }

}
