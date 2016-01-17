package fr.spaceproject.vessels;

import java.util.Map;

import fr.spaceproject.utils.Vec2f;

public class VesselAI
{	
	public VesselAI()
	{
	}
	
	public float getAnglesDifference(Vec2f angle1, Vec2f angle2)
	{
		float dot = angle1.x * angle2.x + angle1.y * angle2.y;
		float mag_v1 = (float)Math.sqrt( angle1.x * angle1.x + angle1.y * angle1.y );
		float mag_v2 = (float)Math.sqrt( angle2.x * angle2.x + angle2.y * angle2.y );
		
		float cosa = dot / (mag_v1 * mag_v2);
		
		return (float)Math.acos(cosa);
	}
	
	public void update(Vessel vessel, Vessel ennemyVessel, Map<VesselAction, Boolean> currentActions, float angleMovement)
	{
		Vec2f distanceVessels = new Vec2f(vessel.getPosition().x - ennemyVessel.getPosition().x, vessel.getPosition().y - ennemyVessel.getPosition().y);
		float distanceBeetweenVessels = distanceVessels.getLength();
		distanceVessels.normalize(1);
		
		
		/*if (distanceBeetweenVessels > 500)
			currentActions.put(VesselAction.MoveForward, true);
		else if (distanceBeetweenVessels < 300)
			currentActions.put(VesselAction.MoveBackward, true);*/
		
		
		Vec2f sightVector = new Vec2f(-1, 0);
		sightVector.rotate(vessel.getAngle());
		
		float angle = (float)Math.toDegrees(getAnglesDifference(distanceVessels, sightVector)) - 90;
		
		if (angle > 0.1)
			currentActions.put(VesselAction.TurnLeft, true);
		else if (angle < -0.1)
			currentActions.put(VesselAction.TurnRight, true);
		
		
		if (Math.abs(Math.toDegrees(getAnglesDifference(distanceVessels, sightVector)) - 90) < 10 && distanceBeetweenVessels < 600 && distanceBeetweenVessels > 200)
			currentActions.put(VesselAction.Shoot, true);
	}
}
