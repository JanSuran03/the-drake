package thedrake;

public class Board {

    public Board(int dimension) {
    }

    public int dimension() {
    }

    public BoardTile at(TilePos pos) {
    }

    public Board withTiles(TileAt... ats) {
    }

    public PositionFactory positionFactory() {
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

