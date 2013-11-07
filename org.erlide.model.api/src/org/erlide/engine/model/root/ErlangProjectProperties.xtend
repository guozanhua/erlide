package org.erlide.engine.model.root

import com.google.common.base.Charsets
import com.google.common.collect.Lists
import java.util.Collection
import java.util.Collections
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path
import org.erlide.runtime.api.RuntimeCore
import org.erlide.runtime.runtimeinfo.RuntimeVersion
import java.nio.charset.Charset
import com.google.common.base.Objects

class ErlangProjectProperties {
    @Property IPath outputDir
    @Property Collection<IPath> sourceDirs
    @Property Collection<IPath> includeDirs
    @Property String externalIncludesFile
    @Property String externalModulesFile
    @Property RuntimeVersion requiredRuntimeVersion
    @Property String runtimeName
    @Property boolean nukeOutputOnClean
    @Property Charset encoding

    new() {
        _sourceDirs = PathSerializer.unpackList(ProjectPreferencesConstants.DEFAULT_SOURCE_DIRS)
        _outputDir = new Path(ProjectPreferencesConstants.DEFAULT_OUTPUT_DIR)
        _includeDirs = PathSerializer.unpackList(ProjectPreferencesConstants.DEFAULT_INCLUDE_DIRS)
        _externalIncludesFile = ProjectPreferencesConstants.DEFAULT_EXTERNAL_INCLUDES
        _externalModulesFile = ProjectPreferencesConstants.DEFAULT_EXTERNAL_MODULES
        _requiredRuntimeVersion = new RuntimeVersion(ProjectPreferencesConstants.DEFAULT_RUNTIME_VERSION)
        _runtimeName = null
        _nukeOutputOnClean = false
        if (_requiredRuntimeVersion.isCompatible(new RuntimeVersion(18))) {
            _encoding = Charsets.UTF_8
        } else {
            _encoding = Charsets.ISO_8859_1
        }
    }

    def getIncludeDirs() {
        Collections.unmodifiableCollection(_includeDirs)
    }

    def setIncludeDirs(Collection<IPath> includeDirs2) {
        _includeDirs = Lists.newArrayList(includeDirs2)
    }

    def setIncludeDirs(IPath... includeDirs2) {
        _includeDirs = Lists.newArrayList(includeDirs2)
    }

    def getSourceDirs() {
        Collections.unmodifiableCollection(_sourceDirs)
    }

    def setSourceDirs(Collection<IPath> sourceDirs2) {
        _sourceDirs = Lists.newArrayList(sourceDirs2)
    }

    def setSourceDirs(IPath... sourceDirs2) {
        _sourceDirs = Lists.newArrayList(sourceDirs2)
    }

    def copyFrom(ErlangProjectProperties erlangProjectProperties) {
        val bprefs = erlangProjectProperties
        _includeDirs = bprefs._includeDirs
        _sourceDirs = bprefs._sourceDirs
        _outputDir = bprefs._outputDir
        _runtimeName = bprefs._runtimeName
        _requiredRuntimeVersion = bprefs._requiredRuntimeVersion
    }

    def getRuntimeInfo() {
        val runtime = RuntimeCore.runtimeInfoCatalog.getRuntime(_requiredRuntimeVersion, _runtimeName)
        runtime
    }

    def getRuntimeVersion() {

        // XXX ???
        val runtimeInfo = runtimeInfo
        if (runtimeInfo !== null) {
            runtimeInfo.version
        } else {
            _requiredRuntimeVersion
        }
    }

    def setRuntimeVersion(RuntimeVersion runtimeVersion) {
        this._requiredRuntimeVersion = runtimeVersion
        if (_requiredRuntimeVersion.isCompatible(new RuntimeVersion(18))) {
            _encoding = Charsets.UTF_8
        } else {
            _encoding = Charsets.ISO_8859_1
        }
    }

    @Deprecated def getRuntimeName() {
        _runtimeName
    }

    @Deprecated def setRuntimeName(String runtimeName) {
        this._runtimeName = runtimeName
    }

    def boolean sameAs(Object other1) {
        if (this === other1)
            return true
        if (other1 === null)
            return false
        if (!(other1 instanceof ErlangProjectProperties))
            return false
        val other = other1 as ErlangProjectProperties
        if (_outputDir === null) {
            if (other._outputDir !== null)
                return false
        } else if (!_outputDir.equals(other._outputDir))
            return false
        if (_sourceDirs === null) {
            if (other._sourceDirs !== null)
                return false
        } else if (!_sourceDirs.equals(other._sourceDirs))
            return false
        if (_includeDirs === null) {
            if (other._includeDirs !== null)
                return false
        } else if (!_includeDirs.equals(other._includeDirs))
            return false
        if (_externalIncludesFile === null) {
            if (other._externalIncludesFile !== null)
                return false
        } else if (!_externalIncludesFile.equals(other._externalIncludesFile))
            return false
        if (_externalModulesFile === null) {
            if (other._externalModulesFile !== null)
                return false
        } else if (!_externalModulesFile.equals(other._externalModulesFile))
            return false
        if (_requiredRuntimeVersion === null) {
            if (other._requiredRuntimeVersion !== null)
                return false
        } else if (!_requiredRuntimeVersion.equals(other._requiredRuntimeVersion))
            return false
        if (_runtimeName === null) {
            if (other._runtimeName !== null)
                return false
        } else if (!_runtimeName.equals(other._runtimeName))
            return false
        if (other._nukeOutputOnClean != _nukeOutputOnClean)
            return false
        if (_encoding === null) {
            if (other._encoding !== null)
                return false
        } else if (!_encoding.equals(other._encoding))
            return false
        return true
    }

    override toString() {
        val helper = Objects.toStringHelper(this) => [
            add("outputDir", _outputDir)
            add("sources", _sourceDirs)
            add("includes", _includeDirs)
            add("runtimeVersion", _requiredRuntimeVersion)
        ]
        helper.toString
    }

}
