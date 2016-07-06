package fr.spaceproject.vessels;

import java.util.Map;
import java.util.Vector;

import fr.spaceproject.utils.Vec2f;


public class VesselAI {
	Vessel targetVessel;
	boolean goToMapCenter;
	Vec2f sectorSize;


	public VesselAI(Vec2f sectorSize) {
		targetVessel = null;
		goToMapCenter = false;
		this.sectorSize = sectorSize;
	}

	private float getAnglesDifference(Vec2f angle1, Vec2f angle2) {
		float dot = angle1.x * angle2.x + angle1.y * angle2.y;
		float mag_v1 = (float) Math.sqrt(angle1.x * angle1.x + angle1.y * angle1.y);
		float mag_v2 = (float) Math.sqrt(angle2.x * angle2.x + angle2.y * angle2.y);

		float cosa = dot / (mag_v1 * mag_v2);

		return (float) Math.acos(cosa);
	}

	public void update(Vector<Vessel> vessels, Vessel vessel, Map<VesselAction, Boolean> currentActions, int[] factionsAgressivity, float angleMovement) {
		if (targetVessel == null || targetVessel.isDestroyed() || targetVessel.getFaction() == vessel.getFaction() || (targetVessel.getFaction() == 0 && factionsAgressivity[vessel.getFaction()] < 50))
			targetVessel = getClosestVessel(vessels, vessel, factionsAgressivity);

		if (vessel.getPosition().getDistance(new Vec2f(0, 0)) > Math.min(sectorSize.x / 2, sectorSize.y / 2) * 4 / 5 || vessel.getSpeed().getLength() < 100)
			goToMapCenter = true;
		else if (vessel.getPosition().getDistance(new Vec2f(0, 0)) < Math.min(sectorSize.x / 2, sectorSize.y / 2) * 3 / 4)
			goToMapCenter = false;

		if (targetVessel != null || goToMapCenter) {
			Vec2f targetPosition = (goToMapCenter || targetVessel == null ? new Vec2f() : targetVessel.getPosition());

			Vec2f distanceVessels = new Vec2f(vessel.getPosition().x - targetPosition.x, vessel.getPosition().y - targetPosition.y);
			float distanceBeetweenVessels = distanceVessels.getLength();
			distanceVessels.normalize(1);


			if (distanceBeetweenVessels > 500)
				currentActions.put(VesselAction.MoveForward, true);
			else if (distanceBeetweenVessels < 300)
				currentActions.put(VesselAction.MoveBackward, true);

			focusPoint(targetPosition, vessel, currentActions, angleMovement);

			Vec2f sightVector = new Vec2f(-1, 0);
			sightVector.rotate(vessel.getAngle());

			if (!goToMapCenter && Math.abs(Math.toDegrees(getAnglesDifference(distanceVessels, sightVector)) - 90) < 10 && distanceBeetweenVessels < 600 && distanceBeetweenVessels > 200)
				currentActions.put(VesselAction.Shoot, true);
		}
	}

	private Vessel getClosestVessel(Vector<Vessel> vessels, Vessel vessel, int[] factionsAgressivity) {
		Vessel closestVessel = null;
		float closestVesselDistance = Float.MAX_VALUE;

		for (int i = 0; i < vessels.size(); ++i) {
			float vesselDistance = vessel.getPosition().getDistance(vessels.get(i).getPosition());

			if (vessel != vessels.get(i) && vesselDistance < closestVesselDistance && !vessels.get(i).isDestroyed() && vessels.get(i).getFaction() != vessel.getFaction()) {
				if (vessels.get(i).getFaction() != 0 || (vessels.get(i).getFaction() == 0 && factionsAgressivity[vessel.getFaction()] >= 50)) {
					closestVessel = vessels.get(i);
					closestVesselDistance = vesselDistance;
				}
			}
		}

		return closestVessel;
	}

	private void focusPoint(Vec2f point, Vessel vessel, Map<VesselAction, Boolean> currentActions, float angleMovement) {
		Vec2f distancePoint = new Vec2f(vessel.getPosition().x - point.x, vessel.getPosition().y - point.y);
		Vec2f sightVector = new Vec2f(-1, 0);
		sightVector.rotate(vessel.getAngle());

		float angle = (float) Math.toDegrees(getAnglesDifference(distancePoint, sightVector)) - 90;

		if (angle > 1)
			currentActions.put(VesselAction.TurnLeft, true);
		else if (angle < 1)
			currentActions.put(VesselAction.TurnRight, true);
	}
}
