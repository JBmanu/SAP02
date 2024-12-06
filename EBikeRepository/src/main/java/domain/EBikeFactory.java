package domain;

import concreate.EBikeImpl;

public interface EBikeFactory {
    EBike createDefault();

    EBike create(String eBikeId);

    class SimpleFactory implements EBikeFactory {
        int id = 0;

        @Override
        public EBike createDefault() {
            return new EBikeImpl(this.id++ + "");
        }

        @Override
        public EBike create(final String eBikeId) {
            return new EBikeImpl(eBikeId);
        }
    }
}
