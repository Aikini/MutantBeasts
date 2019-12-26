package chumbanotz.mutantbeasts.client.renderer.entity.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import chumbanotz.mutantbeasts.MutantBeasts;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

@OnlyIn(Dist.CLIENT)
public class EndersoulHandModel extends Model {
	public static final ResourceLocation DEFAULT_LOCATION = MutantBeasts.prefix("item/endersoul_hand");
	public static final ResourceLocation MODEL_LOCATION = MutantBeasts.prefix("item/endersoul_hand_model");
	private final RendererModel hand;
	private final RendererModel[] finger = new RendererModel[3];
	private final RendererModel[] foreFinger = new RendererModel[3];
	private final RendererModel thumb;

	public EndersoulHandModel() {
		this.textureWidth = 32;
		this.hand = new RendererModel(this);
		this.hand.setRotationPoint(0.0F, 17.5F, 0.0F);
		float fingerScale = 0.6F;

		int i;
		for (i = 0; i < this.finger.length; ++i) {
			this.finger[i] = new RendererModel(this, i * 4, 0);
			this.finger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale);
		}

		this.finger[0].setRotationPoint(-0.5F, 0.0F, -1.0F);
		this.finger[1].setRotationPoint(-0.5F, 0.0F, 0.0F);
		this.finger[2].setRotationPoint(-0.5F, 0.0F, 1.0F);

		for (i = 0; i < this.foreFinger.length; ++i) {
			this.foreFinger[i] = new RendererModel(this, 1 + i * 5, 0);
			this.foreFinger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale - 0.01F);
			this.foreFinger[i].setRotationPoint(0.0F, 0.5F + (float)(i == 1 ? 6 : 5), 0.0F);
		}

		for (i = 0; i < this.finger.length; ++i) {
			this.hand.addChild(this.finger[i]);
			this.finger[i].addChild(this.foreFinger[i]);
		}

		this.thumb = new RendererModel(this, 14, 0);
		this.thumb.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1, fingerScale);
		this.thumb.setRotationPoint(0.5F, 0.0F, -0.5F);
		this.hand.addChild(this.thumb);
	}

	private void resetAngles(RendererModel model) {
		model.rotateAngleX = 0.0F;
		model.rotateAngleY = 0.0F;
		model.rotateAngleZ = 0.0F;
	}

	public void setAngles() {
		this.resetAngles(this.hand);

		for (int i = 0; i < this.finger.length; ++i) {
			this.resetAngles(this.finger[i]);
			this.resetAngles(this.foreFinger[i]);
		}

		this.resetAngles(this.thumb);
		this.hand.rotateAngleY = -0.3926991F;
		this.finger[0].rotateAngleX = -0.2617994F;
		this.finger[1].rotateAngleZ = 0.17453294F;
		this.finger[2].rotateAngleX = 0.2617994F;
		this.foreFinger[0].rotateAngleZ = -0.2617994F;
		this.foreFinger[1].rotateAngleZ = -0.3926991F;
		this.foreFinger[2].rotateAngleZ = -0.2617994F;
		this.thumb.rotateAngleX = -0.62831855F;
		this.thumb.rotateAngleZ = -0.3926991F;
	}

	public void render() {
		this.setAngles();
		this.hand.render(0.0625F);
	}

	public static class Loader implements ICustomModelLoader {
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
		}

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return MODEL_LOCATION.equals(modelLocation);
		}

		@Override
		public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
			return new EndersoulHandModel.Unbaked();
		}
	}

	@OnlyIn(Dist.CLIENT)
	static class Unbaked implements IUnbakedModel {
		private static final List<ResourceLocation> DEPENDENCIES = Arrays.asList(DEFAULT_LOCATION, MODEL_LOCATION);
		@Override
		public IBakedModel bake(ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format) {
			try {
				IBakedModel originalModel = ModelLoaderRegistry.getModel(DEFAULT_LOCATION).bake(bakery, spriteGetter, sprite, format);
				IBakedModel bakedModel = ModelLoaderRegistry.getModel(MODEL_LOCATION).bake(bakery, spriteGetter, sprite, format);
				return new EndersoulHandModel.Baked(originalModel, bakedModel);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Collection<ResourceLocation> getDependencies() {
			return DEPENDENCIES;
		}

		@Override
		public Collection<ResourceLocation> getTextures(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors) {
			return Collections.emptyList();
		}
	}

	@OnlyIn(Dist.CLIENT)
	static class Baked extends BakedModelWrapper<IBakedModel> {
		private final IBakedModel bakedModel;

		public Baked(IBakedModel originalModel, IBakedModel bakedModel) {
			super(originalModel);
			this.bakedModel = bakedModel;
		}

		@SuppressWarnings("deprecation")
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType cameraTransformType) {
			switch (cameraTransformType) {
			case GUI:
			case FIXED:
			case GROUND:
				return super.handlePerspective(cameraTransformType);
			default:
				return this.bakedModel.handlePerspective(cameraTransformType);
			}
		}
	}
}