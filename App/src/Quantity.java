public class Quantity {

    // Enum (base = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // Value Object
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null)
                throw new IllegalArgumentException("Unit cannot be null");

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid value");

            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // 🔹 PRIVATE HELPER (DRY)
        private static QuantityLength addInternal(
                QuantityLength a,
                QuantityLength b,
                LengthUnit targetUnit) {

            double sumFeet = a.toFeet() + b.toFeet();
            double result = targetUnit.fromFeet(sumFeet);

            return new QuantityLength(result, targetUnit);
        }

        // 🔹 UC6 (old method - keeps compatibility)
        public QuantityLength add(QuantityLength other) {
            if (other == null)
                throw new IllegalArgumentException("Other cannot be null");

            return addInternal(this, other, this.unit);
        }

        // 🔹 UC7 (NEW - explicit target unit)
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            return addInternal(this, other, targetUnit);
        }

        // 🔹 STATIC VERSION
        public static QuantityLength add(
                QuantityLength a,
                QuantityLength b,
                LengthUnit targetUnit) {

            if (a == null || b == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            return addInternal(a, b, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            return Math.abs(this.toFeet() - other.toFeet()) < 1e-6;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Feet result: " + a.add(b, LengthUnit.FEET));
        System.out.println("Inches result: " + a.add(b, LengthUnit.INCHES));
        System.out.println("Yards result: " + a.add(b, LengthUnit.YARDS));

        QuantityLength c = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("CM result: " + c.add(d, LengthUnit.CENTIMETERS));
    }
}