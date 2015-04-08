package communication.messaging;

import learning.Model;
import learning.models.LogisticModel;
import privacy.learning.DifferentialLogisticModel;

/**
 * Created by alex on 3/9/15.
 */
public class Message {
    private Model _model;
    private String _content;

    public Message(Model model) {
        _model = model;
    }

    public Message(String content) {
        _content = content;
    }

    public Message() {

    }

    public Model getModel() {
        return _model;
    }

    public String getContent() {
        return _content;
    }
}
