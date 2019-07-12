public class Pair {
        private Integer first;
        private Integer second;

        public Pair(Integer first, Integer second) {
            this.first = first;
            this.second = second;
        }

        public Integer getFirst() {
            return first;
        }

        public Integer getSecond() {
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
