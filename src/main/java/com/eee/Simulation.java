package com.eee;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Simulation {
    int width;
    int height;
    int granica=0;  // ustawione domyslnie na granice martwa
    Cell[][] board;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new Cell[width][height];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
                this.board[i][j] = new Cell(0,204,204,204);
        }
    }

    public int getState(int x, int y) {
        if ( x < 0 || x >= width) {
            return 0;
        }
        if ( y < 0 || y >= height) {
            return 0;
        }
        return this.board[x][y].state;
    }

    public void setState(int x, int y, int state, int r, int g, int b){
        if ( x < 0 || x >= width) {
            return;
        }
        if ( y < 0 || y >= height) {
            return;
        }
        this.board[x][y].state = state;
        if(state == 0)
        {
            this.board[x][y].r = 204;
            this.board[x][y].g = 204;
            this.board[x][y].b = 204;
        }
        else if(state == 1)
        {
            this.board[x][y].r = r;
            this.board[x][y].g = g;
            this.board[x][y].b = b;
        }
        else if (state == 2)
        {
            this.board[x][y].r = 127;
            this.board[x][y].g = 255;
            this.board[x][y].b = 212;
        }
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

        if(getState(x-1,y-1) == 1)
        count += 1;
        if(getState(x,y-1) == 1)
        count += 1;
        if(getState(x+1,y-1) == 1)
        count += 1;

        if(getState(x-1,y) == 1)
        count += 1;
        if(getState(x+1,y) == 1)
        count += 1;

        if(getState(x-1,y+1) == 1)
        count += 1;
        if(getState(x,y+1) == 1)
        count += 1;
        if(getState(x+1,y+1) == 1)
        count += 1;

        return count;
    }
    String decToBin(int x)
    {
        char [] binC = {'0','0','0','0','0','0','0','0'};
        int digit = 7;
        while(x > 0)
        {
            if(x%2 == 1)
                binC[digit] = '1';
            x/=2;
            digit--;
        }
        return new String(binC);
    }
    String rgbToBin(int r, int g, int b)
    {
        return decToBin(r) + decToBin(g) + decToBin(b);
    }
    int binToDec(char [] bin, int start)
    {
        int res = 0;
        for(int i = 7; i >=0; i--){
            if(bin[start+i] == '1')
                res += Math.pow(2, 7-i);
        }
        return res;
    }
    int[] getNewColor(int x, int y)
    {
        Cell[] parents = new Cell[3];
        int[] rgb = new int[3];
        int count = 0;
        if(granica==1) {
            for(int i = 0; i < 3; i++)
                parents[i] = new Cell(1, ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256));
        }
        else{
            if(getState(x-1,y-1) == 1){
                parents[count] = this.board[x-1][y-1];
                count += 1;
            }
            if(getState(x,y-1) == 1){
                parents[count] = this.board[x][y-1];
                count += 1;
            }
            if(getState(x+1,y-1) == 1){
                parents[count] = this.board[x+1][y-1];
                count += 1;
            }

            if(getState(x-1,y) == 1){
                parents[count] = this.board[x-1][y];
                count += 1;
            }
            if(getState(x+1,y) == 1){
                parents[count] = this.board[x+1][y];
                count += 1;
            }

            if(getState(x-1,y+1) == 1){
                parents[count] = this.board[x-1][y+1];
                count += 1;
            }
            if(getState(x,y+1) == 1){
                parents[count] = this.board[x][y+1];
                count += 1;
            }
            if(getState(x+1,y+1) == 1){
                parents[count] = this.board[x+1][y+1];
                count += 1;
            }
        }
        Integer [] chooseParents;
        if(count == 2){
            chooseParents = new Integer[] {0,1};
        }
        else{
            chooseParents = new Integer[]{0,1,2};
        }
        
        List<Integer> parentsList = Arrays.asList(chooseParents);
        Collections.shuffle(parentsList);
        parentsList.toArray(chooseParents);

        Integer [] bitsToSwap = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        List<Integer> bitsList = Arrays.asList(bitsToSwap);
        Collections.shuffle(bitsList);
        bitsList.toArray(bitsToSwap);

        char [] parent1 = rgbToBin(parents[chooseParents[0]].r, parents[chooseParents[0]].g, parents[chooseParents[0]].b).toCharArray();
        char [] parent2 = rgbToBin(parents[chooseParents[1]].r, parents[chooseParents[1]].g, parents[chooseParents[1]].b).toCharArray();

        int n = ThreadLocalRandom.current().nextInt(0, 24);
        for(int i = 0; i < n; i++)
		    parent1[bitsToSwap[i]] = parent2[bitsToSwap[i]];
        
        rgb[0] = binToDec(parent1, 0);
        rgb[1] = binToDec(parent1, 8);
        rgb[2] = binToDec(parent1, 16);
        return rgb;
    }

    public void step ()  {
        Cell[][] newBoard = new Cell[width][height];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
                newBoard[j][i] = new Cell(0,204,204,204);
        }
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width; x++) {
                int aliveNeighbours = countAliveNeighbours(x,y);
                int[] rgb;
                if(getState(x, y) == 1){
                    if(aliveNeighbours < 2 || aliveNeighbours > 3) {
                        newBoard[x][y].state = 0;
                        newBoard[x][y].r = 204;
                        newBoard[x][y].g = 204;
                        newBoard[x][y].b = 204;
                    } else if (aliveNeighbours == 2 || aliveNeighbours == 3) {
                        newBoard[x][y].state = 1;
                        rgb = getNewColor(x, y);
                        newBoard[x][y].r = rgb[0];
                        newBoard[x][y].g = rgb[1];
                        newBoard[x][y].b = rgb[2];
                    }
                } else if (getState(x, y) == 0) {
                    if (aliveNeighbours == 3) {
                        newBoard[x][y].state = 1;
                        rgb = getNewColor(x, y);
                        newBoard[x][y].r = rgb[0];
                        newBoard[x][y].g = rgb[1];
                        newBoard[x][y].b = rgb[2];
                    }
                } if (getState(x, y) == 2) {
                    newBoard[x][y].state = 2;
                    newBoard[x][y].r = 127;
                    newBoard[x][y].g = 255;
                    newBoard[x][y].b = 212;
                }
            }
        }
        this.board = newBoard;
    }
}
class Cell
{
    int state;
    int r, g, b;
    public Cell(int state, int r, int g, int b)
    {
        this.state = state;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}