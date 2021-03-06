package auction.core.handlers;

import auction.datasources.Datasource;
import auction.datasources.MongoDatasource;
import auction.events.EventBase;
import com.lmax.disruptor.EventHandler;
import org.bson.Document;

public class BusinessLogicHandler implements EventHandler {

    private Datasource datasource;

    public BusinessLogicHandler() {
        datasource = MongoDatasource.getInstance();
    }

    @Override
    public void onEvent(Object event, long sequence, boolean endOfBatch) {

        EventBase base = (EventBase) event;
        try {
            base.getEvent().applyTo(base.getTarget());
        } catch (Exception ex) {
            datasource.insertError(new Document().append("type", ex.toString()).append("message", ex.getMessage()));
        }
    }
}
