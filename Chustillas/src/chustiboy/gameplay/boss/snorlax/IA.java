package chustiboy.gameplay.boss.snorlax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.ai.btree.decorator.AlwaysSucceed;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import chustiboy.Partida;
import chustiboy.net.packets.boss.Packet_boss_position;

public class IA {
	BigBigMaloMaloso boss;
	BehaviorTree<BigBigMaloMaloso> behaviourTree;

	public IA(BigBigMaloMaloso boss) {
		this.boss = boss;
		boss.hp = 50;
		
		behaviourTree = new BehaviorTree<>();
		behaviourTree.setObject(boss);
		
		Selector<BigBigMaloMaloso> fireballs = new Selector<>();
		fireballs.addChild(new Probability<BigBigMaloMaloso>(0.95f, new Task_SimpleFireball()));
		fireballs.addChild(new Probability<BigBigMaloMaloso>(0.5f, new Task_FireballsVertical()));
		fireballs.addChild(new Task_FireballsCorner());
		
		Sequence<BigBigMaloMaloso> attack = new Sequence<>();
		attack.addChild(new AlwaysSucceed<BigBigMaloMaloso>(new Probability<BigBigMaloMaloso>(0.0005f, new Task_Casita())));
		attack.addChild(new AlwaysSucceed<BigBigMaloMaloso>(new Probability<BigBigMaloMaloso>(0.0035f, new Task_Tron())));
		attack.addChild(new AlwaysSucceed<BigBigMaloMaloso>(new Probability<BigBigMaloMaloso>(0.0035f, new Task_FirePuddle())));
		attack.addChild(new Probability<BigBigMaloMaloso>(0.015f, fireballs));
		
		Sequence<BigBigMaloMaloso> charge = new Sequence<>();
		charge.addChild(new Task_Timer(15f));
		charge.addChild(new Task_Charge());
		charge.addChild(new Task_Stomp());
		
		Selector<BigBigMaloMaloso> base = new Selector<>();
		base.addChild(charge);
		base.addChild(attack);
		
		behaviourTree.addChild(base);
	}
	
	public void hit(short pj) {
		if(--boss.hp <= 0) {
			boss.die();
		}
	}
	
	public void update() {
		behaviourTree.step();
	}
}

class Task_Timer extends LeafTask<BigBigMaloMaloso> {

	private float initialTime;
	
	Task_Timer(float initialTime) {
		this.initialTime = initialTime;
	}
	
	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		if(boss.charging) return Status.SUCCEEDED;
		
		boss.phase_timer -= Gdx.graphics.getDeltaTime();
		
		if(boss.phase_timer <= 0) {
			boss.charging = true;
			boss.phase_timer = initialTime;
			boss.stomp_stop();
			
			return Status.SUCCEEDED;
		}
	
		return Status.FAILED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_Charge extends LeafTask<BigBigMaloMaloso> {
	
	private Vector2 tmp = new Vector2();
	private float distance_threshold2 = 5f, v = 3;
	
	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		if(boss.target == null && boss.charging) {
			boss.target = Partida.chustillas.get(MathUtils.random(Partida.chustillas.size-1));
		}
		
		tmp.set(boss.getPosition()).sub(boss.target.getPosition());
		if(tmp.len2() > distance_threshold2) {
			tmp.set(boss.getPosition().sub(tmp.nor().scl(v)));
			boss.setPosition(tmp.x, tmp.y);
			
			Packet_boss_position p = new Packet_boss_position();
			p.boss_id = boss.id;
			p.x = tmp.x;
			p.y = tmp.y;
			boss.net.sendTCP(p);
			
			return Status.FAILED;
		} else {
			boss.target = null;
			boss.charging = false;
			return Status.SUCCEEDED;
		}
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_Stomp extends LeafTask<BigBigMaloMaloso> {
	
	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		boss.stomp(boss.getPosition().x, boss.getPosition().y);
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_SimpleFireball extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		int i = MathUtils.random(Partida.chustillas.size-1);
		Vector2 dir = new Vector2(Partida.chustillas.get(i).getPosition()).sub(boss.getPosition()).nor();
		boss.shootFireball(boss.getPosition(), dir);
	
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_FireballsVertical extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		Vector2 pos = new Vector2(0, Partida.stage_height);
		Vector2 dir = new Vector2(0, -1);
		
		int separacion = 50;
		int n = Partida.stage_width / separacion;
		n = Math.min(n, boss.fireballs.pool.size);
		
		for(int i = n; i >= 0; i--) {
			boss.shootFireball(pos, dir);
			pos.x += separacion;
		}
		
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_FireballsCorner extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		if(boss.fireballs.isPoolEmpty()) return Status.FAILED;
		
		Vector2 pos = new Vector2(10, 10);
		Vector2 dir = new Vector2();
		float angle = 0;
		
		int n = Math.min(15, boss.fireballs.pool.size);
		float step = 90 / n;
		
		for(int i = n; i >= 0; i--) {
			dir.x = MathUtils.cosDeg(angle);
			dir.y = MathUtils.sinDeg(angle);
			
			boss.shootFireball(pos, dir);
			angle += step;
		}
		
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_Tron extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		boss.startTron();
		
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_FirePuddle extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		if(boss.casita.active) return Status.FAILED;
		
		int min_size = 10;
		int max_size = 300;
		int width = MathUtils.random(min_size, max_size);
		int height = MathUtils.random(min_size, max_size);
		float x = MathUtils.random(width/2, Partida.stage_width-width/2);
		float y = MathUtils.random(0, Partida.stage_height-height);
		
		boss.spawnFirePuddle(x, y, width, height);
		
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Task_Casita extends LeafTask<BigBigMaloMaloso> {

	@Override
	public Status execute() {
		BigBigMaloMaloso boss = getObject();
		
		int centroWidth = 300, centroHeight = 260;
		float x = MathUtils.random(centroWidth/2, Partida.stage_width-centroWidth/2);
		float y = MathUtils.random(0, Partida.stage_height-centroHeight);
		
		boss.startCasita(x, y);
		
		return Status.SUCCEEDED;
	}

	@Override
	protected Task<BigBigMaloMaloso> copyTo(Task<BigBigMaloMaloso> task) {
		return task;
	}
}

class Probability<E> extends Sequence<E> {
	Probability(float probability, Task<E> task) {
		super();
		this.addChild(new ProbabilityTask<E>(probability));
		this.addChild(task);
	}
}

class ProbabilityTask<E> extends LeafTask<E> {

	private float probability;
	
	ProbabilityTask(float probability) {
		this.probability = probability;
	}
	
	@Override
	public Status execute() {
		if(MathUtils.randomBoolean(probability))
			return Status.SUCCEEDED;
		else return Status.FAILED;
	}

	@Override
	protected Task<E> copyTo(Task<E> task) {
		return task;
	}
}
