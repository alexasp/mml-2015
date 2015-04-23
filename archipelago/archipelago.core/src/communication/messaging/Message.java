package communication.messaging;

import learning.ParametricModel;

/**
 * Created by alex on 3/9/15.
 */
public class Message {
    private ParametricModel _model;
    private String _content;

    public Message(ParametricModel model) {
        _model = model;
    }

    public Message(String content) {
        _content = content;
    }

    public Message() {

    }

    public ParametricModel getModel() {
        return _model;
    }

    public String getContent() {
        return _content;
    }
}
