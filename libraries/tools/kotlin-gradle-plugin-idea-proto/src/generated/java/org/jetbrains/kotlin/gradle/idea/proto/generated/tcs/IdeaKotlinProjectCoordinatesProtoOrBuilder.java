// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto_tcs.proto

package org.jetbrains.kotlin.gradle.idea.proto.generated.tcs;

public interface IdeaKotlinProjectCoordinatesProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.gradle.idea.proto.generated.tcs.IdeaKotlinProjectCoordinatesProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Renamed from 'build_id' to 'build_name' in 1.9.20
   * </pre>
   *
   * <code>optional string build_name = 1;</code>
   * @return Whether the buildName field is set.
   */
  boolean hasBuildName();
  /**
   * <pre>
   * Renamed from 'build_id' to 'build_name' in 1.9.20
   * </pre>
   *
   * <code>optional string build_name = 1;</code>
   * @return The buildName.
   */
  java.lang.String getBuildName();
  /**
   * <pre>
   * Renamed from 'build_id' to 'build_name' in 1.9.20
   * </pre>
   *
   * <code>optional string build_name = 1;</code>
   * @return The bytes for buildName.
   */
  com.google.protobuf.ByteString
      getBuildNameBytes();

  /**
   * <pre>
   * Added in 1.9.20
   * </pre>
   *
   * <code>optional string build_path = 4;</code>
   * @return Whether the buildPath field is set.
   */
  boolean hasBuildPath();
  /**
   * <pre>
   * Added in 1.9.20
   * </pre>
   *
   * <code>optional string build_path = 4;</code>
   * @return The buildPath.
   */
  java.lang.String getBuildPath();
  /**
   * <pre>
   * Added in 1.9.20
   * </pre>
   *
   * <code>optional string build_path = 4;</code>
   * @return The bytes for buildPath.
   */
  com.google.protobuf.ByteString
      getBuildPathBytes();

  /**
   * <code>optional string project_path = 2;</code>
   * @return Whether the projectPath field is set.
   */
  boolean hasProjectPath();
  /**
   * <code>optional string project_path = 2;</code>
   * @return The projectPath.
   */
  java.lang.String getProjectPath();
  /**
   * <code>optional string project_path = 2;</code>
   * @return The bytes for projectPath.
   */
  com.google.protobuf.ByteString
      getProjectPathBytes();

  /**
   * <code>optional string project_name = 3;</code>
   * @return Whether the projectName field is set.
   */
  boolean hasProjectName();
  /**
   * <code>optional string project_name = 3;</code>
   * @return The projectName.
   */
  java.lang.String getProjectName();
  /**
   * <code>optional string project_name = 3;</code>
   * @return The bytes for projectName.
   */
  com.google.protobuf.ByteString
      getProjectNameBytes();
}
