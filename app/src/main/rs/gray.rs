#pragma  version (1)
#pragma  rs  java_package_name(l3.techno.imageeditorl3)

uchar4  RS_KERNEL  toGray(uchar4  in) {

    float4  pixelf = rsUnpackColor8888(in);
    float  gray = (0.30f* pixelf.r+ 0.59f* pixelf.g+ 0.11f* pixelf.b);
    return  rsPackColorTo8888(gray , gray , gray , pixelf.a);

}