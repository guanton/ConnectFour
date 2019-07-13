public class Pair {
        private Number first;
        private Number second;

        public Pair(Number first, Number second) {
            this.first = first;
            this.second = second;
        }

        public Number getFirst() {
            return first;
        }

        public Number getSecond() {
            return second;
        }

        public int hashCode() {
            int result = 17;
            result = 31 * result + first.hashCode();
            result = 31 * result + second.hashCode();
            return result;
        }

        public boolean equals(Object other) {
            if (other instanceof Pair) {
                Pair otherPair = (Pair) other;
                return this.first == otherPair.first && this.second == otherPair.second;
            }
            return false;
        }





}
