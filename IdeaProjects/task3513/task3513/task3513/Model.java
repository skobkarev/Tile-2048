package task3513;

import java.util.*;

public class Model {

    private static final int FIELD_WIDTH = 4; // размер поля
    private Tile[][] gameTiles;               // игровое поле
    int score;                                // счет
    int maxTile;                              // значение макимальной плитки
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
        this.score = 0;
        this.maxTile = 2;
    }

    // вовзращает лист пустых клеток
    private List<Tile> getEmptyTiles() {
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value == 0) result.add(gameTiles[i][j]);
            }
        }
        return result;
    }

    // добавляет рандомно клетку 2 или 4 (соотношение 1 к 9)
    void addTile() {

        List<Tile> list = getEmptyTiles();
        if (list != null && list.size() != 0) {
            list.get((int) (list.size() * Math.random())).setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    // сброс всех клеток
    void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                this.gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    // сжатие одного ряда влево
    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        Tile temp;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[j].getValue() == 0 && tiles[j + 1].getValue() != 0) {
                    temp = tiles[j];
                    tiles[j] = tiles[j + 1];
                    tiles[j + 1] = temp;
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    // сложение клеток
    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int j = 0; j < 3; j++) {
            if (tiles[j].getValue() != 0 && tiles[j].getValue() == tiles[j + 1].getValue()) {
                tiles[j].setValue(tiles[j].getValue() * 2);
                tiles[j + 1].setValue(0);
                if (tiles[j].getValue() > maxTile) maxTile = tiles[j].getValue();
                score += tiles[j].getValue();
                isChanged = true;
            }
        }

        if (isChanged) {
            Tile temp;
            for (int j = 0; j < 3; j++) {
                if (tiles[j].getValue() == 0 && tiles[j + 1].getValue() != 0) {
                    temp = tiles[j];
                    tiles[j] = tiles[j + 1];
                    tiles[j + 1] = temp;
                }
            }
        }
        return isChanged;
    }


    public void left() {
        if(isSaveNeeded) saveState(gameTiles);
        boolean isChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                isChanged = true;
            }
        }
        if (isChanged) {
            addTile();
            isSaveNeeded = true;
        }
    }


    public void up() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public void down() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }


    private void rotate() {
        int len = FIELD_WIDTH;
        for (int k = 0; k < len / 2; k++) // border -> center
        {
            for (int j = k; j < len - 1 - k; j++) // left -> right
            {
                Tile tmp = gameTiles[k][j];
                gameTiles[k][j] = gameTiles[j][len - 1 - k];
                gameTiles[j][len - 1 - k] = gameTiles[len - 1 - k][len - 1 - j];
                gameTiles[len - 1 - k][len - 1 - j] = gameTiles[len - 1 - j][k];
                gameTiles[len - 1 - j][k] = tmp;
            }
        }
    }

    // геттер для поля
    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    // проверка возможности хода
    public boolean canMove() {
        if (!getEmptyTiles().isEmpty())
            return true;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 1; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j - 1].value)
                    return true;
            }
        }
        for (int j = 0; j < gameTiles.length; j++) {
            for (int i = 1; i < gameTiles.length; i++) {
                if (gameTiles[i][j].value == gameTiles[i - 1][j].value)
                    return true;
            }
        }
        return false;
    }

    //сохранение предыдущего состояния и поля
    private void saveState (Tile[][] tiles) {
        Tile[][]temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for(int i = 0; i< FIELD_WIDTH; i++) {
            for(int j = 0; j< FIELD_WIDTH; j++) {
                int value = tiles[i][j].value;
                temp[i][j] = new Tile(value);
            }
        }
        int newScore = score;
        previousStates.push(temp);
        previousScores.push(newScore);
        isSaveNeeded = false;
    }

    public void rollback() {
        if(previousStates.size() !=0  &&  previousScores.size() != 0) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }



    public void randomMove() {
        int n = (int)((Math.random() * 100) % 4);
        switch (n) {
            case 0 : left();
            break;
            case 1 : up();
            break;
            case 2 : right();
            break;
            case 3 : down();
            break;
        }
    }



    public MoveEfficiency getMoveEfficiency(Move move) {
//
        move.move();
        int empty = getEmptyTiles().size();
        int fakeScore = score;
        MoveEfficiency moveEfficiency = null;
        if(!hasBoardChanged()) { moveEfficiency = new MoveEfficiency(-1 ,0, move); }
        else { moveEfficiency = new MoveEfficiency(empty, fakeScore, move); }
        rollback();
        return moveEfficiency;

    }

    //сравниваем вес плиток в СТЭКЕ и ИГРОВОМ ПОЛЕ
    public boolean hasBoardChanged() {

       int tilesWeightGame = 0;
        int tilesWeightStack = 0;

       for(Tile[] tiles : gameTiles) {
           for(Tile t : tiles) {
               tilesWeightGame = t.value + tilesWeightGame;
           }
       }

        for(Tile[] tiles : previousStates.peek()) {
            for(Tile t : tiles) {
                tilesWeightStack = t.value + tilesWeightStack;
            }
        }

        return  (tilesWeightGame != tilesWeightStack);
    }


    public void autoMove() {
        PriorityQueue <MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());

        priorityQueue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                left();
            }
        }));

        priorityQueue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                right();
            }
        }));

        priorityQueue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                up();
            }
        }));

        priorityQueue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                down();
            }
        }));

        MoveEfficiency moveEfficiency = priorityQueue.peek();
        Move move = moveEfficiency.getMove();
        move.move();

    }


}
