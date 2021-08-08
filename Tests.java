import static org.junit.Assert.*;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JLabel;

import org.junit.Test;

public class Tests {

	@Test
	public void decreasePointsSpeciaPointTest() {
		SpecialPoint s = new DecreasePoints(0, 0, 700, 400, Color.BLUE);
		assertEquals(5, s.pointChanger(10));
	}
	
	@Test
	public void deathPointSpeciaPointTest() {
		SpecialPoint s = new DeathPoint(0, 0, 700, 400, Color.BLUE);
		assertEquals(0, s.pointChanger(10));
	}
	
	@Test
	public void DoublePointSpeciaPointTest() {
		SpecialPoint s = new TriplePoints(0, 0, 700, 400, Color.BLUE);
		assertEquals(30, s.pointChanger(10));
	}
	
	@Test
	public void GameCourtInitialStateTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		assertEquals(3, g.getSpecialPointsListSize());
		assertEquals(1, g.getSnakeLength());
		assertEquals(0, g.getTurnsLength());
	}
	
	@Test
	public void GameCourtIntersectRegularPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy());
		g.setPointPx(point.getPx());
		g.setPointPy(point.getPy());
		g.tick();
		point = new Point(point.getPx(), point.getPy(), 700, 400, Color.RED);
		assertTrue(g.headIntersectsPoint(point));
		assertEquals(2, g.getSnakeLength());
		assertEquals(1, g.getScore());
	}
	
	@Test
	public void GameCourtNoIntersectRegularPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		g.setHeadPx(10);
		g.setHeadPy(10);
		g.setPointPx(100);
		g.setPointPy(100);
		g.tick();
		assertFalse(g.headIntersectsPoint());
		assertEquals(1, g.getSnakeLength());
		assertEquals(0, g.getScore());
	}
	
	@Test
	public void GameCourtHitsWallTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		g.setHeadPx(700);
		g.setHeadPy(400);
		g.setHeadVx(10);
		g.tick();
		assertEquals(Direction.RIGHT, g.headHitsWall());
		assertFalse(g.getPlaying());
	}
	
	@Test
	public void GameCourtIntersectsBodyPartTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy());
		g.tick();
		g.tick();
		assertEquals(2, g.getSnakeLength());
		g.setHeadPy(g.getHeadPy()- point.getHeight());
		g.tick();
		assertTrue(g.headIntersectsTail());
		assertFalse(g.getPlaying());
	}
	
	@Test
	public void GameCourtPosNewTailToTheLeftTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx()-10);
		g.setHeadPy(point.getPy());
		g.setHeadVx(10);
		g.tick();
		assertEquals(g.getTailPx(), g.getHeadPx() - point.getWidth());
		assertTrue(g.headIntersectsPoint(point));
		assertEquals(2, g.getSnakeLength());
		assertEquals(1, g.getScore());
	}
	
	@Test
	public void GameCourtPosNewTailToTheRightTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx()+10);
		g.setHeadPy(point.getPy());
		g.setHeadVx(-10);
		g.tick();
		assertEquals(g.getTailPx(), g.getHeadPx() + point.getWidth());
		assertTrue(g.headIntersectsPoint(point));
		assertEquals(2, g.getSnakeLength());
		assertEquals(1, g.getScore());
	}
	
	@Test
	public void GameCourtPosNewTailOnTopTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy()-10);
		g.setHeadVy(10);
		g.tick();
		assertEquals(g.getTailPy(), g.getHeadPy() - point.getHeight());
		assertTrue(g.headIntersectsPoint(point));
		assertEquals(2, g.getSnakeLength());
		assertEquals(1, g.getScore());
	}
	
	@Test
	public void GameCourtPosNewTailToTheBottomTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy()+10);
		g.setHeadVy(-10);
		g.tick();
		assertEquals(g.getTailPy(), g.getHeadPy() + point.getHeight());
		assertTrue(g.headIntersectsPoint(point));
		assertEquals(2, g.getSnakeLength());
		assertEquals(1, g.getScore());
	}
	
	@Test
	public void GameCourtTailReachesTurnTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx()-10);
		g.setHeadPy(point.getPy());
		g.setHeadVx(10);
		g.tick();
		point = new Point(point.getPx(), point.getPy(), 700, 400, Color.RED);
		assertTrue(g.headIntersectsPoint(point));
		LinkedList<Turn> turns = g.getTurns();
		Turn newTurn = new Turn(g.getHeadPx(), g.getHeadPy(), 0, 10, 700, 400);
		turns.add(newTurn);
		g.setTurns(turns);
		assertEquals(1, g.getTurnsLength());
		g.tick();
		assertEquals(g.getTailVx(), 0);
		assertEquals(g.getTailVy(), 10);
		assertEquals(0, g.getTurnsLength());
	}
	
	@Test
	public void GameCourtIntersectTripleSpecialPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy());
		g.tick();
		SpecialPoint specialPoint = new TriplePoints(g.getHeadPx(), g.getHeadPy(), 
				700, 400, Color.BLUE);
		g.setCurrentSpecialPoint(specialPoint);
		g.tick();
		assertTrue(g.headIntersectsSpecialPoint(specialPoint));
		assertEquals(2, g.getSnakeLength());
		assertEquals(3, g.getScore());
	}
	
	@Test
	public void GameCourtIntersectDecreaseSpecialPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy());
		g.tick();
		SpecialPoint specialPoint = new DecreasePoints(g.getHeadPx(), g.getHeadPy(), 
				700, 400, Color.BLUE);
		g.setCurrentSpecialPoint(specialPoint);
		g.tick();
		assertTrue(g.headIntersectsSpecialPoint(specialPoint));
		assertEquals(2, g.getSnakeLength());
		assertEquals(0, g.getScore());
	}
	
	@Test
	public void GameCourtIntersectDeathSpecialPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(point.getPx());
		g.setHeadPy(point.getPy());
		g.tick();
		SpecialPoint specialPoint = new DeathPoint(g.getHeadPx(), g.getHeadPy(), 
				700, 400, Color.BLUE);
		g.setCurrentSpecialPoint(specialPoint);
		g.tick();
		assertTrue(g.headIntersectsSpecialPoint(specialPoint));
		assertEquals(2, g.getSnakeLength());
		assertEquals(0, g.getScore());
		assertFalse(g.getPlaying());
	}
	
	@Test
	public void GameCourtNoIntersectSpecialPointTest() {
		GameCourt g = new GameCourt(new JLabel(), new JLabel());
		g.reset();
		Point point = g.getPoint();
		g.setHeadPx(10);
		g.setHeadPy(10);
		SpecialPoint specialPoint = new TriplePoints(g.getHeadPx(), g.getHeadPy(), 
				700, 400, Color.BLUE);
		g.setCurrentSpecialPoint(specialPoint);
		g.setSpecialPointPx(30);
		g.setSpecialPointPy(30);
		g.tick();
		assertFalse(g.headIntersectsSpecialPoint(specialPoint));
		assertEquals(1, g.getSnakeLength());
		assertEquals(0, g.getScore());
	}
}
