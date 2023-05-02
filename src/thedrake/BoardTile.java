package thedrake;

import java.util.Collections;
import java.util.List;

public interface BoardTile extends Tile {
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
    };
}
