package entity.ebike;

import entity.ebike.concreate.EBikeImpl;

public interface EBikeFactory {
    EBike createDefault();

    class SimpleFactory implements EBikeFactory {
        int id = 0;

        @Override
        public EBike createDefault() {
            return new EBikeImpl(this.id++ + "");
        }
    }
}
