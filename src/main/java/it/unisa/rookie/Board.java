package it.unisa.rookie;

import it.unisa.rookie.piece.Bishop;
import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.King;
import it.unisa.rookie.piece.Knight;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import it.unisa.rookie.piece.Queen;
import it.unisa.rookie.piece.Rook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Piece> boardPositions;
  private Player currentPlayer;
  private Player opponentPlayer;
  private Move transition;
  private ArrayList<Piece> whitePieces;
  private ArrayList<Piece> blackPieces;
  private ArrayList<Move> whitePlayerLegalMoves;
  private ArrayList<Move> blackPlayerLegalMoves;

  public Board(Map<Integer, Piece> boardPositions, Color currentPlayerColor, Move transition) {
    this.boardPositions = boardPositions;
    this.transition = transition;
    Piece currentPlayerKing = null;
    Piece opponentPlayerKing = null;
    this.whitePieces = new ArrayList<>();
    this.blackPieces = new ArrayList<>();

    for (Piece p : boardPositions.values()) {
      if (p.getColor() == Color.WHITE) {
        whitePieces.add(p);
      } else {
        blackPieces.add(p);
      }

      if (p.getType() == ChessPieceType.KING) {
        if (p.getColor() == currentPlayerColor) {
          currentPlayerKing = p;
        } else {
          opponentPlayerKing = p;
        }
      }
    }

    this.whitePlayerLegalMoves = this.getLegalMoves(this.whitePieces);
    this.blackPlayerLegalMoves = this.getLegalMoves(this.blackPieces);

    // Current player / Opponent player creation
    Color opponentPlayerColor = (currentPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
    ArrayList<Move> currentPlayerLegalMoves =
            (currentPlayerColor == Color.WHITE) ? whitePlayerLegalMoves : blackPlayerLegalMoves;
    ArrayList<Move> opponentPlayerLegalMoves =
            (currentPlayerColor == Color.WHITE) ? blackPlayerLegalMoves : whitePlayerLegalMoves;


    boolean isCurrentPlayerKingInCheck = !(getThreats(
            currentPlayerKing.getPosition().getValue(),
            opponentPlayerLegalMoves
    ).isEmpty());

    boolean isOpponentPlayerKingInCheck = !(getThreats(
            opponentPlayerKing.getPosition().getValue(),
            currentPlayerLegalMoves
    ).isEmpty());

    this.currentPlayer = new Player(
            currentPlayerColor,
            isCurrentPlayerKingInCheck
    );
    this.opponentPlayer = new Player(
            opponentPlayerColor,
            isOpponentPlayerKingInCheck
    );
  }

  // Creates a "standard" starting board
  public Board(Color startingPlayerColor) {
    boardPositions = new HashMap<>(32, 1.0f);

    this.blackPieces = new ArrayList<>();
    this.whitePieces = new ArrayList<>();

    blackPieces.add(new Rook(Color.BLACK, Position.A8));
    blackPieces.add(new Knight(Color.BLACK, Position.B8));
    blackPieces.add(new Bishop(Color.BLACK, Position.C8));
    blackPieces.add(new Queen(Color.BLACK, Position.D8));
    blackPieces.add(new King(Color.BLACK, Position.E8));
    blackPieces.add(new Bishop(Color.BLACK, Position.F8));
    blackPieces.add(new Knight(Color.BLACK, Position.G8));
    blackPieces.add(new Rook(Color.BLACK, Position.H8));
    blackPieces.add(new Pawn(Color.BLACK, Position.A7));
    blackPieces.add(new Pawn(Color.BLACK, Position.B7));
    blackPieces.add(new Pawn(Color.BLACK, Position.C7));
    blackPieces.add(new Pawn(Color.BLACK, Position.D7));
    blackPieces.add(new Pawn(Color.BLACK, Position.E7));
    blackPieces.add(new Pawn(Color.BLACK, Position.F7));
    blackPieces.add(new Pawn(Color.BLACK, Position.G7));
    blackPieces.add(new Pawn(Color.BLACK, Position.H7));

    whitePieces.add(new Pawn(Color.WHITE, Position.A2));
    whitePieces.add(new Pawn(Color.WHITE, Position.B2));
    whitePieces.add(new Pawn(Color.WHITE, Position.C2));
    whitePieces.add(new Pawn(Color.WHITE, Position.D2));
    whitePieces.add(new Pawn(Color.WHITE, Position.E2));
    whitePieces.add(new Pawn(Color.WHITE, Position.F2));
    whitePieces.add(new Pawn(Color.WHITE, Position.G2));
    whitePieces.add(new Pawn(Color.WHITE, Position.H2));
    whitePieces.add(new Rook(Color.WHITE, Position.A1));
    whitePieces.add(new Knight(Color.WHITE, Position.B1));
    whitePieces.add(new Bishop(Color.WHITE, Position.C1));
    whitePieces.add(new Queen(Color.WHITE, Position.D1));
    whitePieces.add(new King(Color.WHITE, Position.E1));
    whitePieces.add(new Bishop(Color.WHITE, Position.F1));
    whitePieces.add(new Knight(Color.WHITE, Position.G1));
    whitePieces.add(new Rook(Color.WHITE, Position.H1));

    /*
    blackPieces.add(new King(Color.BLACK, Position.E8));
    blackPieces.add(new Pawn(Color.BLACK, Position.D3));
    whitePieces.add(new King(Color.WHITE, Position.E1));
    whitePieces.add(new Pawn(Color.WHITE, Position.F6));
    */

    for (Piece p : blackPieces) {
      putPiece(p);
    }

    for (Piece p : whitePieces) {
      putPiece(p);
    }

    this.whitePlayerLegalMoves = this.getLegalMoves(this.whitePieces);
    this.blackPlayerLegalMoves = this.getLegalMoves(this.blackPieces);

    this.currentPlayer = new Player(
            startingPlayerColor,
            false
    );
    this.opponentPlayer = new Player(
            (startingPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE,
            false
    );
  }

  public Map<Integer, Piece> getBoardPositions() {
    return boardPositions;
  }

  public void setBoardPositions(Map<Integer, Piece> boardPositions) {
    this.boardPositions = boardPositions;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public Player getOpponentPlayer() {
    return opponentPlayer;
  }

  public void setOpponentPlayer(Player opponentPlayer) {
    this.opponentPlayer = opponentPlayer;
  }

  public Move getTransition() {
    return transition;
  }

  public void setTransition(Move transition) {
    this.transition = transition;
  }

  public ArrayList<Piece> getWhitePieces() {
    return whitePieces;
  }

  public void setWhitePieces(ArrayList<Piece> whitePieces) {
    this.whitePieces = whitePieces;
  }

  public ArrayList<Piece> getBlackPieces() {
    return blackPieces;
  }

  public void setBlackPieces(ArrayList<Piece> blackPieces) {
    this.blackPieces = blackPieces;
  }

  public ArrayList<Move> getWhitePlayerLegalMoves() {
    return whitePlayerLegalMoves;
  }

  public void setWhitePlayerLegalMoves(ArrayList<Move> whitePlayerLegalMoves) {
    this.whitePlayerLegalMoves = whitePlayerLegalMoves;
  }

  public ArrayList<Move> getBlackPlayerLegalMoves() {
    return blackPlayerLegalMoves;
  }

  public void setBlackPlayerLegalMoves(ArrayList<Move> blackPlayerLegalMoves) {
    this.blackPlayerLegalMoves = blackPlayerLegalMoves;
  }

  public ArrayList<Move> getAllPossibleLegalMoves() {
    ArrayList<Move> moves = new ArrayList<>();
    moves.addAll(this.whitePlayerLegalMoves);
    moves.addAll(this.blackPlayerLegalMoves);
    return moves;
  }

  public void putPiece(Piece p) {
    this.boardPositions.put(p.getPosition().getValue(), p);
  }

  public void removePiece(Integer pos) {
    boardPositions.remove(pos);
  }

  public Piece getPiece(Integer pos) {
    return this.boardPositions.get(pos);
  }

  private ArrayList<Move> getLegalMoves(ArrayList<Piece> pieces) {
    ArrayList<Move> legalMoves = new ArrayList<>();
    for (Piece p : pieces) {
      ArrayList<Move> singlePieceLegalMoves = (ArrayList<Move>) p.getLegalMoves(this);
      legalMoves.addAll(singlePieceLegalMoves);
    }
    return legalMoves;
  }

  private ArrayList<Move> getThreats(int tile, ArrayList<Move> candidateThreats) {
    ArrayList<Move> result = new ArrayList<>();
    for (Move threat : candidateThreats) {
      if (threat.getDestination().getValue() == tile) {
        result.add(threat);
      }
    }
    return result;
  }

  public boolean isCheckMateAvoidable(Color playerColor) {
    Player p = this.currentPlayer.getPlayerColor() == playerColor
            ? this.getCurrentPlayer() : this.getOpponentPlayer();

    if (p.isKingInCheck()) {
      ArrayList<Move> currentPlayerLegalMoves =
              playerColor == Color.WHITE ? whitePlayerLegalMoves : blackPlayerLegalMoves;

      // Look for a move that can "free" the king
      for (Move m : currentPlayerLegalMoves) {
        Board nextBoard = m.makeMove();
        // ...found a move!
        if (!nextBoard.getOpponentPlayer().isKingInCheck()) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public String toString() {
    return "Board{"
            + "boardPositions=" + boardPositions
            + ", currentPlayer=" + currentPlayer
            + ", whitePlayerLegalMoves=" + whitePlayerLegalMoves
            + ", blackPlayerLegalMoves=" + blackPlayerLegalMoves
            + ", whitePieces=" + whitePieces
            + ", blackPieces=" + blackPieces
            + "}";
  }
}
