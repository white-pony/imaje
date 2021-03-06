(ns imaje.wrapper
	(import java.io.File)
	(import java.awt.image.BufferedImage)
	(import javax.imageio.ImageIO))

(defn width
	""
	[image]
	(.getWidth image))

(defn height
	""
	[image]
	(.getHeight image))

(defn empty-image
	""
	([^BufferedImage image]
	(empty-image (width image) (height image)))
	([wd hg]
	(BufferedImage. wd hg BufferedImage/TYPE_INT_RGB)))

(defn load-image
	""
	[filename]
	(ImageIO/read (File. filename)))

(defn save-image
	""
	[img filename]
	(ImageIO/write img "png" (File. filename)))

(defn get-pixels 
 	""
  	([image]
    	(.getDataElements (.getRaster image) 0 0 (width image) (height image) nil)))

(defn set-pixels!
 	""
  	([image pixels]
    	(.setDataElements (.getRaster image) 0 0 (.getWidth image) (.getHeight image) (int-array pixels))))

(defn get-type
	"Returns a type of an image"
	[image]
	(.getType image))

(defn get-rgb
	"Returns an integer pixel in the default RGB color model"
	[^BufferedImage image x y]
	(.getRGB image x y)
	)

(defn set-rgb!
	"Sets a pixel in this BufferedImage to the specified RGB value."
	[^BufferedImage image x y rgb]
	(.setRGB image x y rgb)
	)
