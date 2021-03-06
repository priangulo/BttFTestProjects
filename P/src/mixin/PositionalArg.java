package mixin;

import Jakarta.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Vector;

@mixin.R4Feature(mixin.R4Feature.kernel)
public class PositionalArg extends CommandLineArg {

    @mixin.R4Feature(mixin.R4Feature.kernel)

    String binding;

    public PositionalArg( String _name, int _layer ) {
        name = _name;
        layerID = _layer;
        optional = false;
    }
}
