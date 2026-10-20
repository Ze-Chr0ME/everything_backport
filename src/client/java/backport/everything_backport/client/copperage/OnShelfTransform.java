package backport.everything_backport.client.copperage;

import net.minecraft.client.renderer.block.model.ItemTransform;

/**
 * 1.21.9's {@code on_shelf} item display context. 1.21.7 has no such context, so the transform is stored next to the
 * others on {@code ItemTransforms} and used in place of {@code fixed} while a shelf renders its items.
 */
public interface OnShelfTransform {
	ItemTransform everythingBackport$getOnShelf();

	void everythingBackport$setOnShelf(ItemTransform transform);

	final class Context {
		private static boolean renderingShelf;

		private Context() {
		}

		public static boolean isRenderingShelf() {
			return renderingShelf;
		}

		public static void setRenderingShelf(boolean rendering) {
			renderingShelf = rendering;
		}
	}
}
