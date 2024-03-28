package kr.easw.lesson3;

import java.util.Random;
import java.util.Scanner;

public class SnakeGameWithoutTails {

    private static final int BOARD_SIZE = 10;
    // 0 - 빈 타일
    // 1 - 스네이크 블럭
    // 2 - 아이템
    private static final int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    private static final Random RANDOM = new Random();

    private static int score = 0;

    private static SnakeLocation location = new SnakeLocation(0, 0);

    public static void main(String[] args) {
        initializeBoard(); // 보드 초기화
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printBoard();
            System.out.print("[우측 (r) | 좌측 (l) | 위 (u) | 아래 (d) | 종료 (0) ] : ");
            if (!nextDirection(scanner.next())) {
                System.out.println("게임 오버!");
                System.out.printf("점수: %d\n", score);
                break;
            }
            if (!hasItemOnBoard())
                placeRandomItem();
        }
    }

    private static void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0; // 모든 타일을 빈 타일로 초기화
            }
        }
        // 스네이크 초기 위치 설정
        int startX = RANDOM.nextInt(BOARD_SIZE);
        int startY = RANDOM.nextInt(BOARD_SIZE);
        location = new SnakeLocation(startX, startY);
        board[startX][startY] = 1; // 스네이크 위치를 1로 설정
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 사용자의 입력을 받고, 다음 위치로 옮기거나 게임을 종료해야 합니다.
     * <p>
     * 허용되는 입력은 다음과 같습니다 :
     * - 우측(r) | 좌측 (l) | 위 (u) | 아래 (d) | 종료 (0)
     * <p>
     * 다음 좌표는 location 변수에 계속해서 업데이트되어야 합니다.
     * 만약 다음 좌표에 아이템이 존재한다면, 점수를 1 증가하고 다음 좌표의 값을 0으로 되돌려야 합니다.
     *
     * 만약 값이 최대 값 (BOARD_SIZE)이상이 되거나 최소 값(0) 아래로 내려간다면 같은 좌표로 설정하여 이동하지 않도록 해야합니다.
     *
     * 만약 사용자의 입력이 종료(0)였다면, false값을 반환하여 게임을 종료해야 합니다.
     */
    private static boolean nextDirection(String keyword) {
        int newX = location.getX();
        int newY = location.getY();

        switch (keyword) {
            case "r":
                newY++;
                break;
            case "l":
                newY--;
                break;
            case "u":
                newX--;
                break;
            case "d":
                newX++;
                break;
            case "0":
                return false; // 게임 종료
            default:
                System.out.println("잘못된 입력입니다.");
                return true; // 게임 계속 진행
        }

        // 경계를 벗어나지 않도록 제어
        newX = Math.max(0, Math.min(newX, BOARD_SIZE - 1));
        newY = Math.max(0, Math.min(newY, BOARD_SIZE - 1));

        // 아이템을 먹었는지 확인하고 처리
        if (board[newX][newY] == 2) {
            score++; // 점수 증가
            board[newX][newY] = 0; // 아이템 제거
        }

        // 스네이크 위치 업데이트
        board[location.getX()][location.getY()] = 0; // 이전 위치를 빈 타일로 변경
        location = new SnakeLocation(newX, newY);
        board[newX][newY] = 1; // 새로운 위치를 스네이크 블록으로 변경
        
        return true; // 게임 계속 진행
    }

    private static void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (location.getX() == i && location.getY() == j) {
                    System.out.print("◼ ");
                    continue;
                }
                switch (board[i][j]) {
                    case 0:
                        System.out.print("・ ");
                        break;
                    case 1:
                        System.out.print("◼ ");
                        break;
                    case 2:
                        System.out.print("* ");
                        break;
                }
            }
            System.out.println();
        }
    }

    private static void placeRandomItem() {
        while (true) {
            int x = RANDOM.nextInt(BOARD_SIZE);
            int y = RANDOM.nextInt(BOARD_SIZE);
            if (board[x][y] == 0) {
                board[x][y] = 2; // 빈 공간에 아이템 배치
                break;
            }
        }
    }

    private static boolean hasItemOnBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 2) {
                    return true; // 아이템이 보드에 존재함
                }
            }
        }
        return false; // 아이템이 보드에 없음
    }

    private static class SnakeLocation {
        private final int x;
        private final int y;

        public SnakeLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public SnakeLocation adjust(int x, int y) {
            return new SnakeLocation(this.x + x, this.y + y);
        }
    }
}
