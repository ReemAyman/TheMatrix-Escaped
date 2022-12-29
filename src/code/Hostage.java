package code;

public class Hostage extends Position
{
	int damage;

	public Hostage(Position pos, int damage)
	{
		super(pos.x, pos.y);
		this.damage = damage;
	}

	public String toString()
	{
		return x + "," + y + "," + this.damage;
	}
}
