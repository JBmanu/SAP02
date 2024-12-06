package domain;

public interface V2d {
    float x();
    float y();

    V2d sum(float x, float y);
    V2d rotate(float degrees);
    V2d mul(float factor);

    static V2d create(final float x, final float y) {
        record V2dImpl(float x, float y) implements V2d {

            private V2d normalize() {
                final double length = Math.sqrt(this.x() * this.x() + this.y() * this.y());
                return new V2dImpl((float) (this.x() / length), (float) (this.y() / length));
            }

            @Override
            public V2d sum(final float x, final float y) {
                return new V2dImpl(this.x() + x, this.y() + y);
            }

            @Override
            public V2d rotate(final float degrees) {
                final double radians = Math.toRadians(degrees);
                final float x = (float) (this.x() * Math.cos(radians) - this.y() * Math.sin(radians));
                final float y = (float) (this.x() * Math.sin(radians) + this.y() * Math.cos(radians));
                return new V2dImpl(x, y).normalize();
            }

            @Override
            public V2d mul(final float factor) {
                return new V2dImpl(this.x() * factor, this.y() * factor);
            }
        }
        return new V2dImpl(x, y);
    }

}
