using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Threading;
// I know its not the best but i am just writing a stupid stl renderer, not a real application

namespace Matrix_3Dgraphics_Port
{
    internal class Renderer_3D : Form
    {
        private bool Change = true;
        private Sene currSene;

        private Graphics g = null;
        private bool prepChange = true;
        private Matrix<double> prepMatrix;
        private readonly Stack<Matrix<double>> TransformStack = new Stack<Matrix<double>>();
        private viewPort VP;


        public Renderer_3D(Sene s, viewPort v)
        {
            currSene = s;
            VP = v;
            DoubleBuffered = true;
            Width = v.width;
            Height = v.height;
            FormBorderStyle = FormBorderStyle.FixedSingle;
            // resizeing would kind of throw a wrench in the whole pipeline
        }

        private void printPoints(Point[] _points)
        {
            var Xs = new int[_points.Length];
            var Ys = new int[_points.Length];

            for (var i = 0; i < _points.Length; i++)
            {
                Xs[i] = _points[i].X;
                Ys[i] = _points[i].Y;
            }

            Console.WriteLine("[" + string.Join(", ", Xs) + "]");
            Console.WriteLine("[" + string.Join(", ", Ys) + "]\n");
        }

        protected override void OnPaint(PaintEventArgs e)
        {
            if (prepChange)
            {
                // takes verts from object space to world space, then world space to camera space
                var modelViewMat = currSene.WorldMatrix * currSene._Camera.ToCameraCoordsMatrix;

                //projection matrix
                currSene._Camera.MakeProjection(5, 35, Math.PI/2 , 1);
                var P = currSene._Camera.ProjectionMatrix;


                prepMatrix = modelViewMat * P;


                prepChange = false;
                Console.WriteLine($"world matrix: {currSene.WorldMatrix}");
                Console.WriteLine($"camera coords matrix: {currSene._Camera.ToCameraCoordsMatrix}");
                Console.WriteLine($"projection matrix: {currSene._Camera.ProjectionMatrix}");
                Console.WriteLine($"prep matrix: {prepMatrix}");
            }

            // this could all be parallel
            var g = e.Graphics;

            if (Change)
            {
                g.Clear(Color.Black);
                var currCamera = currSene._Camera;

                foreach (var _obj in currSene.objects)
                {
                    //temp for face colors
                    Color[] CubeFaceColors = new Color[6]
                    {
                        Color.Blue,Color.Red,Color.Yellow,Color.Green,Color.Orange,Color.Purple
                    };
                    var ColorIndex = 0;

                    //a long time from now in a galaxy far far away todo: parrallelize faces and possibly objects, maybe go crazy and use a gpu?
                    for (uint f = 0; f < _obj.faces.Length; f++)
                    {
                        var _face = _obj.faces[f];

                        //todo: figure out lighting
                        // light it based on the face normal
                        /*
                        var mat = _obj.mat;
                        var L = currSene.lights;
                        var light = 0.0;

                        for (uint n = 0; n < L.Length; n++)
                        {
                            var Normal = new Vector3(new Vector4(_face.normal.Normalize(), 1).ApplyMatrix(prepMatrix));
                            var LightDir = L[n]._ViewDir;
                            var ViewerDir = (Normal - currCamera.loc).Normalize();

                            Vector3 ReflectedRay;
                            ReflectedRay = (2 * LightDir.dot(Normal) * Normal - LightDir).Normalize();

                            light += mat.ambient_reflection * L[n].ambient_intensity;

                            light += mat.diffuse_reflection * L[n].diffuse_intensity * LightDir.dot(Normal) +
                                     mat.specular_reflection * L[n].specular_intensity *
                                     Math.Pow(ReflectedRay.dot(ViewerDir), mat.shininess);
                        }
                        */


                       
                        //backface culling:
                        var degOffset = Math.Acos(_face.normal.dot(currCamera.viewDir))*180/Math.PI;
                        if (degOffset > -90 && degOffset < 90 )
                        {
                            // this should now be in homogenous clip space
                            ///////RASTERING a triangle\\\\\\\\

                            var points = new Point[3];
                            var verticies = new Vector3[3];
                            bool triangleValid = true;
                            if (TransformStack.Count > 0)
                            {
                                prepMatrix *= TransformStack.Pop();
                            }

                            // far future todo: parralellize the per vertex computation
                            for (int i = 0; i < 3; i++)
                            {
                                double w = 0;
                                verticies[i] = _face.verticies[i].AppyMatrix(prepMatrix, ref w);
                                // verticies should now be in homogenous clip space
                                verticies[i].x /= w;
                                verticies[i].y /= w;
                                verticies[i].z /= w;
                                //after perp div coords should be between -1,1


                                if ((verticies[i].x > -1 && verticies[i].x < 1) &&
                                    (verticies[i].y > -1 && verticies[i].y < 1) &&
                                    w!=0)
                                {
                                    //todo: figure out scaling issue
                                    int x = Math.Min(
                                        VP.width - 1,
                                        (int) (((verticies[i].x * 10) + 1) * 0.5 * VP.width));
                                    int y = Math.Min(
                                        VP.height - 1,
                                        (int) ((1 - ((verticies[i].y * 10) + 1) * 0.5) * VP.height));

                                    points[i] = new Point(x, y);
                                }
                                else
                                {
                                    if ((verticies[i].x < -1 && verticies[i].x > 1) &&
                                        (verticies[i].y < -1 && verticies[i].y > 1))
                                    {
                                        Console.WriteLine("Triangle vertex Invalid!!!");
                                        triangleValid = false;
                                        break;
                                    }

                                    Console.WriteLine("DEGENERATE CASE VERTS LOCS\n" +
                                                      $"W={w}\n");
                                }
                            }


                            if (triangleValid)
                            {
                                Console.WriteLine(
                                    $"face: {_face} has a degree offset of {degOffset*180/Math.PI} degrees\n" +
                                    $" and light value: (don't care yet)\n" +
                                    $"and transformed points: {new Matrix<double>(verticies).ToString()}" +
                                    "the screen projected points are:");
                                printPoints(points);

                                var c = CubeFaceColors[ColorIndex];
                                ColorIndex++;
                                var b = new SolidBrush(c);
                                var p = new Pen(Color.White);
                                g.DrawPolygon(p, points);
                                g.FillPolygon(b, points);
                            }
                            else
                            {
                                Console.WriteLine("triangle invalid!");
                            }
                        }
                    }
                    //todo: have it push triangles to a list, sort list by avg dist to camera, draw in order
                    //to do this we would need to implement a struct that would have all the lighting and vertex data already computed, as well as the avg z dist
                    
               
                }
                //todo: put this on an event that is called and changeds the Change variable
                //push some transform


                //TransformStack.Push(Transform.);
                Thread.Sleep(100);
                Change = false;
            }


            //Invalidate();
        }

        
        [STAThread]
        private static void Main(string[] args)
        {
            Application.EnableVisualStyles();

            // you have to make sene and load it into the constructor           
            var s = new Sene();
            s._Camera = new Camera(new Vector3(40, 0, 0), new Vector3(0, 0, 0));
            s.lights = new[] {new Light(new Vector3(40, 25, 87), new Vector3(0, 0, 0), Color.White, 0.55, 0.9, 0.2)};
            s.WorldMatrix = Matrix<double>.identity(4);
            s.objects = new List<Obj_3d>();
            s.objects.Add(File_handler.STL_LoadFile_3D());

            viewPort v;
            v.height = 1000;
            v.width = 1000;

            Application.Run(new Renderer_3D(s, v));
        }
        

        private void InitializeComponent()
        {
            SuspendLayout();
            // 
            // Renderer_3D
            // 
            ClientSize = new Size(1000, 1000);
            Name = "Renderer_3D";
            Load += Renderer_3D_Load;
            ResumeLayout(false);
        }

        private void Renderer_3D_Load(object sender, EventArgs args)
        {
            //i dont know what goes here
        }

        /* PRODUCTIVITY LIST:
         * DONE: Port/Debug Matrix code
         * DONE: figure out math of projection code
         * DONE: decide how to represent 3d objects
         * DONE: write stl file loader
         * DONE: write/test projection code for a _Camera
         * DONE: test renders w/o shadow or lighting
         * DONE: write blinn-phong or phong lighting model
         * TODO: write flat shading and test that
         
         ////////////// THIS IS FOR LATER\\\\\\\\\\\\\\
         
         * TODO: write vertex shader to calculate lighting at each vertex and write it to a buffer
         * NEXT: test renders w/o shadow
         
         * NEXT: write code that will calculate shadow map
         * NEXT: write code to generate pre-defined geometries, like cubes and other things
         * NEXT: write pixel/fragment shader that will interpolate lighting between the vertices and apply shadow map
         * NEXT: test renders
         
         * NEXT: write obj file loader
         * NEXT: add color support
         */

        /* Puedo-code for gouraund shading:
             * load stl file
             * set viewpoints
             * initialize input event triggers
             * initialize transform stack
             * set framerate
             * on time tick:
             *      apply transformations from the stack to the worldmatrix
             *      update _Camera matrix
             *
             *      note: this can be parrallelized
             *      per triangle/face:
             *          if normal is facing away form _Camera, throw it out
             *          else:
             *              per vertex:
             *                  vetex shader:
             *                      clip vertex if outside of frustrum
             *                      calculate normal of vertex
             *                      calculate brightness based on blinn-phong lighting model
             *                      project to 2d screenspace based on _Camera view:
             *                          apply cameraview matrix, then perspective projection matrix
             *                  save collected data to resource pool
             *
             *      "pixel shader":
             *          look up triangles in 2d screenspace as well as the associated brightness and colors of the verticies
             *          interpolate brightness of blinn-phong calculated verticies across the triangles
             *
             *      transform to light coords
             *      do depth test to z buffer
             *      project zbuffer to _Camera coords, save shadowmap to resouce pool
             *
             *      apply shadow map
             *      push buffer to screen
             *
             * on input event triggers:
             *      combine relevent transforms then put the total transform matrix onto the transform stack 
             */

        /* Puedo-code for flat shading:
         * load stl or obj
         * iterate through faces:
         *      if the normal is away from the _Camera, forgetaboutit
         *      else:
         *          apply transform stack to all verticies
         *          project all verticies based on light perspective and calculate shadowmap as a z-buffer
         *          transform shadowmap to _Camera perspective and save it
         *
         *          project all verticies based on _Camera and world matricies
         *
         *          if normal within 90* of light:
         *              do lighting calculation based on blinn-phong or something
         *              draw polygon with its color + ambient light + specific light
         *
         *          else:
         *              set to ambient color and draw polygon
         *
         *          apply shadowmap
         */

        public struct viewPort
        {
            public int width;
            public int height;
        }
    }
}