package thedrake;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public interface BoardTile extends Tile, JSONSerializable {
    BoardTile EMPTY = new BoardTile() {

        @Override
        public boolean canStepOn() {
            return true;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public String toString() {
            return "empty";
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"empty\"");
        }
    };

    BoardTile MOUNTAIN = new BoardTile() {
        @Override
        public boolean canStepOn() {
            return false;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public String toString() {
            return "mountain";
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"mountain\"");
        }
    };
}
