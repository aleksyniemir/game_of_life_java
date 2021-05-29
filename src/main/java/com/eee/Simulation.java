package com.eee;

public class Simulation {
    int width;
    int height;
    int granica=0;  // ustawione domyslnie na granice martwa
    int[][] board;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
    }

    /*public void printBoard() {
        System.out.println("---");
        for (int y = 0; y < height ; y++) {
            String line = "|";
            for (int x = 0; x < width; x++) {
                if (this.board[x][y] == 0) {
                    line += ".";
                } else {
                    line += "*";
                }
            }
            line += "|";
            System.out.println(line);
        }
        System.out.println("---");
    }
    public void setAlive (int x, int y) {
        this.setState(x, y, 1);
    }
    public void setDead (int x, int y) {
        this.setState(x, y, 0);
    }
    public void setWall (int x, int y) {
        this.board[x][y] = 2;
    }*/

    public int getState(int x, int y) {
        if ( x < 0 || x >= width) {
            return 0;
        }
        if ( y < 0 || y >= height) {
            return 0;
        }
        return this.board[x][y];
    }

    public void setState(int x, int y, int state){
        if ( x < 0 || x >= width) {
            return;
        }
        if ( y < 0 || y >= height) {
            return;
        }
        this.board[x][y] = state;
    }

    public int countAliveNeighbours(int x, int y) {
        int count = 0;

        if(granica==1) {
            if ((x == 0 && y == 0) || (x == 0 && y == height-1) || (x == width-1 && y == 0) || (x == width-1 && y == height-1))
                count += 5;

            if (x == 0 && (y > 0 && y < height-1))
                count += 3;
            if (y == 0 && (x > 0 && x < width-1))
                count += 3;
            if (x == width-1 && (y > 0 && y < height-1))
                count += 3;
            if (y == height-1 && (x > 0 && x < width-1))
                count += 3;
        }

        if(getState(x-1,y-1)!=2)
        count += getState(x-1,y-1);
        if(getState(x,y-1)!=2)
        count += getState(x,y-1);
        if(getState(x+1,y-1)!=2)
        count += getState(x+1,y-1);

        if(getState(x-1,y)!=2)
        count += getState(x-1,y);           //to mozna poprawic pewnie xD
        if(getState(x+1,y)!=2)              // dodane wszedzie ify bo jak byly sciany
        count += getState(x+1,y);           // to dodawalo do counta jakby byly 2 zywe komorki

        if(getState(x-1,y+1)!=2)
        count += getState(x-1,y+1);
        if(getState(x,y+1)!=2)
        count += getState(x,y+1);
        if(getState(x+1,y+1)!=2)
        count += getState(x+1,y+1);

        return count;
    }

    public void step ()  {
        int[][] newBoard = new int[width][height];
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width; x++) {
                int aliveNeighbours = countAliveNeighbours(x,y);

                if(getState(x, y) == 1){
                    if(aliveNeighbours < 2 || aliveNeighbours > 3) {
                        newBoard[x][y] = 0;
                    } else if (aliveNeighbours == 2 || aliveNeighbours == 3) {
                        newBoard[x][y] = 1;
                    }
                } else if (getState(x, y) == 0) {
                    if (aliveNeighbours == 3) {
                        newBoard[x][y] = 1;
                    }
                } else if (getState(x, y) == 2) {
                    newBoard[x][y] = 2;
                }
            }
        }
        this.board = newBoard;
    }
}