package thedrake;

public class Board {
    private final BoardTile[][] boardTiles;
    private final int dimension;


    public Board(int dimension) {
        this.dimension = dimension;
        boardTiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                boardTiles[i][j] = BoardTile.EMPTY;
    }

    public int dimension() {
        return dimension;
    }

    public BoardTile at(BoardPos pos) {
        return boardTiles[pos.i()][pos.j()];
    }

    @Override
    protected Board clone() {
        Board clone = new Board(dimension);
        for (int i = 0; i < dimension; i++)
            clone.boardTiles[i] = boardTiles[i].clone();
        return clone;
    }

    public Board withTiles(TileAt... ats) {
        Board clone = clone();
        for (TileAt tileAt : ats)
            clone.boardTiles[tileAt.pos.i()][tileAt.pos.j()] = tileAt.tile;
        return clone;
    }

    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
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

