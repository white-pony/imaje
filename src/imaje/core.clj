(ns imaje.core
	(import java.io.File)
	(import java.awt.image.BufferedImage)
	(import javax.imageio.ImageIO)
	)

; (set! *warn-on-reflection* true)

(defn width
	""
	[image]
	(.getWidth image)
	)

(defn height
	""
	[image]
	(.getHeight image)
	)

(defn empty-image
	""
	([^BufferedImage image]
	(empty-image (width image) (height image)))
	([wd hg]
	(BufferedImage. wd hg BufferedImage/TYPE_INT_RGB)
	))

(defn load-image
	""
	[filename]
	(ImageIO/read (File. filename))
	)

(defn save-image
	""
	[img filename]
	(ImageIO/write img "png" (File. filename))
	)

(defn get-pixels 
 	""
  	([image]
    	(.getDataElements (.getRaster image) 0 0 (width image) (height image) nil)))

(defn set-pixels!
 	""
  	([image pixels]
    	(.setDataElements (.getRaster image) 0 0 (.getWidth image) (.getHeight image) (int-array pixels))))

(defn create-sampler
	""
	[image]
	(let [pixels (get-pixels image)
			wd (width image)
			hg (height image)]
		(fn [x y] (aget ^ints pixels (+ x (* y wd))))
	)
)

(defn immap
	""
	[fun image]
	(let [wd (width image)
			hg (height image)
			result (empty-image image)
			new-pixels (get-pixels result)
			sampler (create-sampler image)
			]
		(dotimes [y hg]
			(dotimes [x wd]
				(aset ^ints new-pixels (+ x (* wd y)) ^int (fun x y sampler)))
		)
		(set-pixels! result new-pixels)
		result
	)
)

(defn imreduce
	""
	[fun initial image]
	(let [wd (width image)
			hg (height image)
			old-pixels (get-pixels image)
			sampler (create-sampler image)
			]
		(loop [x 0 y 0 accum initial]
			(cond
				(= x wd) (recur 0 (inc y) accum)
				(= y hg) accum
				:else
					(do
						(recur (inc x) y (fun accum x y sampler)))
				)
			)
		)
	)		

(defn pixel-map
	""
	[fn pixels]
	(map fn pixels)
	)

(defn pixel-reduce
	""
	[fn val pixels]
	(reduce fn val pixels)
	)

(defn -main
	[]
	(println "Starting test application")
	(let [img0 (empty-image 640 480)
			img1 (immap #(+ (%3 %1 %2) 12) img0)
			]
		; (time (immap #(+ (%1 %2 %3) 12) img))
		(time (imreduce #(update-in %1 [(%4 %2 %3)] inc) (vec (repeat 256 0)) img1))
		(println (imreduce #(update-in %1 [(%4 %2 %3)] inc) (vec (repeat 256 0)) img1))
		(save-image img1 "out.png")
	)
)
