/*
 * Corona-Warn-App
 *
 * SAP SE and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.server.services.distribution.assembly.structure.directory;

import app.coronawarn.server.services.distribution.assembly.structure.file.File;
import app.coronawarn.server.services.distribution.assembly.structure.functional.DirectoryFunction;
import app.coronawarn.server.services.distribution.assembly.structure.functional.FileFunction;
import app.coronawarn.server.services.distribution.assembly.structure.functional.Formatter;
import app.coronawarn.server.services.distribution.assembly.structure.functional.IndexFunction;
import app.coronawarn.server.services.distribution.assembly.structure.util.ImmutableStack;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class IndexDirectoryImpl<T> extends DirectoryImpl implements IndexDirectory<T> {

  // Files to be written into every directory created through the index
  private final Set<FileFunction> metaFiles = new HashSet<>();
  // Directories to be written into every directory created through the index
  private final Set<DirectoryFunction> metaDirectories = new HashSet<>();
  private final IndexFunction<T> indexFunction;
  private final Formatter<T> indexFormatter;

  /**
   * Constructs a {@link IndexDirectoryImpl} instance that represents a directory, containing an index in the form of
   * sub directories.
   *
   * @param name           The name that this directory should have on disk.
   * @param indexFunction  An {@link IndexFunction} that calculates the index of this {@link IndexDirectoryImpl} from a
   *                       {@link java.util.Stack} of parent {@link IndexDirectoryImpl} indices. The top element of the
   *                       stack is from the closest {@link IndexDirectoryImpl} in the parent chain.
   * @param indexFormatter A {@link Formatter} used to format the directory name for each index element returned by the
   *                       {@link IndexDirectoryImpl#indexFunction}.
   */
  public IndexDirectoryImpl(String name, IndexFunction<T> indexFunction,
      Formatter<T> indexFormatter) {
    super(name);
    this.indexFunction = indexFunction;
    this.indexFormatter = indexFormatter;
  }

  @Override
  public Formatter<T> getIndexFormatter() {
    return this.indexFormatter;
  }

  @Override
  public Set<T> getIndex(ImmutableStack<Object> indices) {
    return this.indexFunction.apply(indices);
  }

  @Override
  public void addFileToAll(FileFunction fileFunction) {
    this.metaFiles.add(fileFunction);
  }

  @Override
  public void addDirectoryToAll(DirectoryFunction directoryFunction) {
    this.metaDirectories.add(directoryFunction);
  }

  /**
   * Creates a new subdirectory for every element of the {@link IndexDirectory#getIndex index} and writes {@link
   * IndexDirectory#addFileToAll files} and {@link IndexDirectory#addDirectory directories} to those. The respective
   * element of the index will be pushed onto the Stack for subsequent
   * {@link app.coronawarn.server.services.distribution.assembly.structure.Writable#prepare} calls on those files and
   * directories.
   *
   * @param indices A {@link Stack} of parameters from all {@link IndexDirectory IndexDirectories} further up in the
   *                hierarchy. The Stack may contain different types, depending on the types {@code T} of {@link
   *                IndexDirectory IndexDirectories} further up in the hierarchy.
   */
  @Override
  public void prepare(ImmutableStack<Object> indices) {
    super.prepare(indices);
    this.prepareIndex(indices);
  }

  private void prepareIndex(ImmutableStack<Object> indices) {
    this.getIndex(indices).forEach(currentIndex -> {
      ImmutableStack<Object> newIndices = indices.push(currentIndex);
      Directory subDirectory = makeSubDirectory(currentIndex);
      prepareMetaFiles(newIndices, subDirectory);
      prepareMetaDirectories(newIndices, subDirectory);
    });
  }

  private Directory makeSubDirectory(T index) {
    Directory subDirectory = new DirectoryImpl(this.indexFormatter.apply(index).toString());
    this.addDirectory(subDirectory);
    return subDirectory;
  }

  private void prepareMetaFiles(ImmutableStack<Object> indices, Directory target) {
    this.metaFiles.forEach(metaFileFunction -> {
      File newFile = metaFileFunction.apply(indices);
      target.addFile(newFile);
      newFile.prepare(indices);
    });
  }

  private void prepareMetaDirectories(ImmutableStack<Object> indices, Directory target) {
    this.metaDirectories.forEach(metaDirectoryFunction -> {
      Directory newDirectory = metaDirectoryFunction.apply(indices);
      target.addDirectory(newDirectory);
      newDirectory.prepare(indices);
    });
  }
}
