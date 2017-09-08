package lumien.resourceloader.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.Level;

import lumien.resourceloader.ResourceLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class NormalResourceLoader implements IResourcePack
{
	boolean debug = false;

	@Override
	public InputStream getInputStream(ResourceLocation rl) throws IOException
	{
		if (!resourceExists(rl))
		{
			return null;
		}
		else
		{
			File file = new File(new File(Minecraft.getMinecraft().mcDataDir, "resources/" + rl.getResourceDomain()), rl.getResourcePath());

			String realFileName = file.getCanonicalFile().getName();
			if (!realFileName.equals(file.getName()))
			{
				ResourceLoader.logger.log(Level.WARN, "[NormalResourceLoader] Resource Location " + rl.toString() + " only matches the file " + realFileName + " because RL is running in an environment that isn't case sensitive in regards to file names. This will not work properly on for example Linux.");
			}

			return new FileInputStream(file);
		}
	}

	@Override
	public boolean resourceExists(ResourceLocation rl)
	{
		File fileRequested = new File(new File(Minecraft.getMinecraft().mcDataDir, "resources/" + rl.getResourceDomain()), rl.getResourcePath());

		if (debug && !fileRequested.isFile())
		{
			ResourceLoader.logger.log(Level.DEBUG, "[NormalResourceLoader] Asked for resource " + rl.toString() + " but can't find a file at " + fileRequested.getAbsolutePath());
		}

		return fileRequested.isFile();
	}

	@Override
	public Set getResourceDomains()
	{
		File folder = new File(Minecraft.getMinecraft().mcDataDir, "resources");
		if (!folder.exists())
		{
			folder.mkdir();
		}
		HashSet<String> folders = new HashSet<String>();

		ResourceLoader.logger.log(Level.DEBUG, "[NormalResourceLoader] Resource Loader Domains: ");

		File[] resourceDomains = folder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

		for (File resourceFolder : resourceDomains)
		{
			if (resourceFolder.getName().equals("debug"))
			{
				debug = true;
			}
		}

		for (File resourceFolder : resourceDomains)
		{
			ResourceLoader.logger.log(Level.DEBUG, "[NormalResourceLoader]  - " + resourceFolder.getName() + " | " + resourceFolder.getAbsolutePath());
			folders.add(resourceFolder.getName());
		}

		return folders;
	}

	@Override
	public IMetadataSection getPackMetadata(MetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
	{
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException
	{
		return null;
	}

	@Override
	public String getPackName()
	{
		return "CustomResources";
	}
}
