package backport.everything_backport.client.copperage;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.client.copperage.model.CopperGolemModel;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class CopperAgeModelLayers {
	public static final ModelLayerLocation COPPER_GOLEM = create("copper_golem");
	public static final ModelLayerLocation COPPER_GOLEM_RUNNING = create("copper_golem_running");
	public static final ModelLayerLocation COPPER_GOLEM_SITTING = create("copper_golem_sitting");
	public static final ModelLayerLocation COPPER_GOLEM_STAR = create("copper_golem_star");
	public static final Map<CopperGolemStatueBlock.Pose, ModelLayerLocation> STATUE_POSES = Map.of(
		CopperGolemStatueBlock.Pose.STANDING,
		COPPER_GOLEM,
		CopperGolemStatueBlock.Pose.SITTING,
		COPPER_GOLEM_SITTING,
		CopperGolemStatueBlock.Pose.STAR,
		COPPER_GOLEM_STAR,
		CopperGolemStatueBlock.Pose.RUNNING,
		COPPER_GOLEM_RUNNING
	);

	private CopperAgeModelLayers() {
	}

	private static ModelLayerLocation create(String name) {
		return new ModelLayerLocation(EverythingBackport.vanilla(name), "main");
	}

	static void init() {
		EntityModelLayerRegistry.registerModelLayer(COPPER_GOLEM, CopperGolemModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(COPPER_GOLEM_RUNNING, CopperGolemModel::createRunningPoseBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(COPPER_GOLEM_SITTING, CopperGolemModel::createSittingPoseBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(COPPER_GOLEM_STAR, CopperGolemModel::createStarPoseBodyLayer);
	}
}
