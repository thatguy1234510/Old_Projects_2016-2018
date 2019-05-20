using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.IO;
using System.Security.AccessControl;

namespace Matrix_3Dgraphics_Port
{
    static class File_handler
    {
        public static Obj_3d STL_LoadFile_3D()
        {
            try
            {

                string ObjectName;
                Obj_3d Object;
                List<Face> faceList=new List<Face>();
                Material DefaultMat = new Material(Color.Blue, 0.95, 0.5, 0.15, 3);

                // Create an instance of StreamReader to read from a file.
                // The using statement also closes the StreamReader.
                using (StreamReader sr = new StreamReader("C:/Users/proff/OneDrive/Documents/Visual Studio 2015/Projects/Matrix_3Dgraphics_Port/Matrix_3Dgraphics_Port/Assets/20mm_cube.stl"))
                {
                    string line;
                    line = sr.ReadLine();
                
                    ObjectName = line.Replace("solid ", "");
                    while (line!=null&&(!line.Contains("endsolid")))
                    {

                        //this is what i will be dealling with and then appending to the object
                        Face currFace;
                        
                        Vector3[] TriangleVertexList=new Vector3[3];
                        Vector3 normal;
                        if (line!=null&&line.Contains("facet normal "))
                        {
                            //if you get the facet normal process it
                            normal = new Vector3(line.Replace("facet normal ", "")).Normalize();

                            //consume the outerloop notifier from the stream
                            line = sr.ReadLine();
                            line = sr.ReadLine();

                            for (int i = 0; i < 3; i++)
                            {

                                string Istring = line.Replace("vertex ", "");
                                TriangleVertexList[i] = new Vector3(Istring);
                                line = sr.ReadLine();
                            }

                            // make a face from our data
                            currFace.verticies = TriangleVertexList;
                            currFace.normal = normal;
                            currFace.vertColors = new Color[3] {Color.Blue, Color.Blue, Color.Blue};
                            //add it to the list
                            faceList.Add(currFace);

                            //consume excess in input stream
                            line = sr.ReadLine();
                            line = sr.ReadLine();

                        }




                        line = sr.ReadLine();
                    }
                }
                Object=new Obj_3d(faceList.ToArray(),DefaultMat,ObjectName);
                return Object;

            }
            catch (Exception e)
            {
                // Let the user know what went wrong.
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
                Console.WriteLine(e.ToString());
            }

            return null;
        }
    }



}
